/**
 * Welcome developer friend. PC ospAdfilex-smartTeleSale-service FileIOServiceImpl.java 5:06:41 PM
 */
package com.business.services.impl;

import com.business.services.FileIOService;
import com.business.services.FileService;
import com.core.config.ApplicationConfig;
import com.core.entity.AttachDocument;
import com.core.entity.VbAttachment;
import com.core.exception.BadRequestException;
import com.core.exception.FileIOException;
import com.core.model.Actor;
import com.core.model.file.*;
import com.core.repository.AttachmentMetadataRepository;
import com.core.repository.VbAttachmentRepository;
import com.core.utils.Constants;
import com.core.utils.StringUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author Nguyen_Toan
 */
@Getter
@Slf4j
@Service
@Transactional
public class FileIOServiceImpl implements FileIOService {


    @Value("${file.validate.blackListExtensions}")
    private String blackListExtensions;

    @Value("${pathFileLog}")
    private String pathFileLog;

    @Value("${fileService.downloadURL}")
    private String downloadURL;

    @Autowired
    private AttachmentMetadataRepository attachmentMetadataRepository;

    @Autowired
    private VbAttachmentRepository vbAttachmentRepository;

    @Autowired
    private FileService fileService;
    @Autowired
    private ApplicationConfig.MessageSourceVi messageSourceVi;

    @Override
    public List<String> getListFile(final String path) {

        File file = new File(path);
        List<String> listFileNames = new ArrayList<>();
        for (String string : Arrays.asList(file.list())) {
            if (string.contains(StringUtils.APPLICATION)) {
                listFileNames.add(string);
            }
        }

        return listFileNames;
    }

    @Override
    public String getContentAsStringFile(final String fileName) throws Exception {

        String data = null;
        File file = new File(pathFileLog);
        String filePath = StringUtils.EMPTY;
        try {

            for (String string : Arrays.asList(file.list())) {
                if (string.equalsIgnoreCase(fileName)) {
                    filePath = new StringBuilder(pathFileLog + StringUtils.SLASH_RIGHT + string).toString();
                    break;
                }
            }

            if (filePath.contains(StringUtils.GZ)) {

                data = readLinesFromGZ(filePath);

            } else {

                Path path = Paths.get(filePath);
                Stream<String> lines = Files.lines(path);
                data = lines.collect(Collectors.joining(StringUtils.NEWLINE_CHARACTER));
                lines.close();
            }
            log.info("*// PATH FILE LOG : " + filePath);
            log.info("*// CONTENT LENGTH = " + data.length());
        } catch (Exception e) {
            throw new FileIOException(
                    messageSourceVi.getMessageVi("ER013", new Object[]{fileName}) + e.getMessage());
        }

        return data;
    }

    @Override
    public FileIOServiceImpl getFileIOServiceImpl() {
        return this;
    }

    private String readLinesFromGZ(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();

        GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(filePath));
        InputStreamReader iReader = new InputStreamReader(gzip);
        BufferedReader bufferedReader = new BufferedReader(iReader);
        String line = null;

        while ((line = bufferedReader.readLine()) != null) {
            content.append(StringUtils.NEWLINE_CHARACTER + line);
        }
        gzip.close();
        iReader.close();
        bufferedReader.close();
        return content.toString();
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadFile(Long attachmentId, DownloadOption downloadOption) {
        VbAttachment attachment;
        attachment = vbAttachmentRepository.findByAttachmentIdAndIsDelete(attachmentId, Constants.DELETE.NORMAL);

        return downloadFromFileService(attachment, downloadOption);
    }

    @Override
    public List<VbAttachment> getFileByObjectIdAndObjectType(Long objectId, Long objectType) {
        List<VbAttachment> listAttachment = vbAttachmentRepository.findByObjectIdAndObjectTypeAndIsDelete(objectId, objectType, Constants.DELETE.NORMAL);
        if(StringUtils.isTrue(listAttachment))
        {
            return listAttachment;
        }
        else return null;
    }

    private ResponseEntity<InputStreamResource> downloadImageConvertFromPdf(VbAttachment vbAttachment, DownloadOption downloadOption) {
        try {
            HttpClient httpClient = HttpClients.createDefault();

            URIBuilder uriBuilder = new URIBuilder(downloadURL + "/" + (StringUtils.isTrue(vbAttachment) ? vbAttachment.getFileServiceId() : downloadOption.getAttachmentMetadataId()));
            uriBuilder
                    .setParameter("type", downloadOption.getType())
                    .setParameter("vbAttachmentId", StringUtils.isTrue(vbAttachment) ? vbAttachment.getAttachmentId().toString() : null)
                    .setParameter("attachmentMetadataId", StringUtils.isTrue(downloadOption.getAttachmentMetadataId()) ? downloadOption.getAttachmentMetadataId().toString() : null)
                    .setParameter("waterMarkText", downloadOption.getWaterMarkText())
                    .setParameter("pageNumber", downloadOption.getPageNumber().toString())
                    .setParameter("imageStatus", downloadOption.getImageStatus());

            HttpGet httpGet = new HttpGet(uriBuilder.build());
            org.apache.http.HttpResponse response = httpClient.execute(httpGet);

            InputStream inputStream = response.getEntity().getContent();
            Header[] filenames = response.getHeaders("filename");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + encodeFileName(StringUtils.isTrue(filenames) ? filenames[0].toString() : ""))
                    .contentType(MediaType.IMAGE_PNG) //
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            throw new RuntimeException("fileService.downloadFail", e);
        }
    }

    private String encodeFileName(String fileName) {
        String encodedFileName;
        try {
            encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return encodedFileName;
    }

    private ResponseEntity<InputStreamResource> downloadFromFileService(
            VbAttachment vbAttachment, DownloadOption downloadOption) {

        String downloadType = downloadOption.getType();

        if (Constants.DOWNLOAD_OPTION.TYPE.IMAGE.equals(downloadType)) {
            return downloadImageConvertFromPdf(vbAttachment, downloadOption);
        }



        InputStream inputStream = fileService.getInputStream(vbAttachment.getFileServiceId(), downloadOption);
        String originalFileName = vbAttachment.getFileName();
        int extensionIndex = originalFileName.lastIndexOf(".");
        String fileName = (extensionIndex == -1
                ? originalFileName
                : originalFileName.substring(0, extensionIndex)) + "."
                + (Constants.DOWNLOAD_OPTION.TYPE.TRANG_KY_SO.equalsIgnoreCase(downloadType)
                ? "png"
                : vbAttachment.getFileExtention());

        String fileNameVB = vbAttachment.getFileName();
        if (StringUtils.isTrue(fileNameVB)) {
            fileName = fileNameVB + "."
                    + (Constants.DOWNLOAD_OPTION.TYPE.TRANG_KY_SO.equalsIgnoreCase(downloadType)
                    ? "png" : vbAttachment.getFileExtention());
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + encodeFileName(fileName))
                .contentType(MediaType.valueOf(vbAttachment.getContentType())) //
                .body(new InputStreamResource(inputStream));
    }

    @Override
    public VbAttachment uploadFile(MultipartFile fileUpoad, Long objectType,  String storageType, String note, Actor actor) {
        VbAttachment uploadResult;
        uploadResult = this.uploadWithFileService(fileUpoad, objectType, storageType, note, actor);
        return uploadResult;
    }

    private void populate(AttachDocument attachDocument, VbAttachment vbAttachment) {
        vbAttachment.setIsEncrypt(attachDocument.getIsEncrypt());
        vbAttachment.setFileServiceId(attachDocument.getAttachDocumentId());
        vbAttachment.setPdfAlready(StringUtils.isTrue(attachDocument.getPdfAttachmentDocumentId()) ? 1L : 0);
    }

    public VbAttachmentExtend uploadWithFileService(MultipartFile multipartFile, Long objectType, String storageType,  String note, Actor actor) {

        String contentType = multipartFile.getContentType();
        boolean needConvertPdf = (Constants.ATTACHMENT_OBJECT_TYPE.VB_DI.equals(objectType))
                && (Constants.CONTENT_TYPE.DOC.equals(contentType) || Constants.CONTENT_TYPE.DOCX.equals(contentType));

        UploadOption uploadOption = new UploadOption();
        uploadOption.setNeedConvertPdf(needConvertPdf);
        uploadOption.setNeedEncrypt(true);

        VbAttachment vbAttachment = new VbAttachment();
        vbAttachment.setObjectType(objectType);
        vbAttachment.setStorageType(storageType);
        String fileName = multipartFile.getOriginalFilename();
        String fileType = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".") + 1).trim().toLowerCase() : "";
        vbAttachment.setFileName(multipartFile.getOriginalFilename());
        vbAttachment.setFileExtention(fileType);
        vbAttachment.setUpdatorId(actor.getUserId());
        vbAttachment.setCreatorId(actor.getUserId());
        vbAttachment.setNote(StringUtils.stringNotTrim(note));
        vbAttachment.setIsDelete(Constants.DELETE.NORMAL);

        vbAttachment.setContentType(contentType);
        vbAttachment.setTypeUser(actor.getType());
        populate(fileService.uploadFile(multipartFile, uploadOption,actor), vbAttachment);
        vbAttachmentRepository.save(vbAttachment);

        // đổ dữ liệu từ thằng vbAttachment gốc sang
        VbAttachmentExtend vbAttachmentExtend = new VbAttachmentExtend(vbAttachment);

        // detect case create new outgoing doc - start
        if ((Constants.ATTACHMENT_OBJECT_TYPE.VB_DI.equals(objectType))
                && contentType.equalsIgnoreCase(Constants.CONTENT_TYPE.PDF)) {
            if (Constants.ATTACHMENT_OBJECT_TYPE.VB_DI.equals(objectType)) {
                ExtractFileResult result = fileService.convertPdfToImage(vbAttachment,actor);
                attachmentMetadataRepository.save(result.getAttachmentMetadata());
                vbAttachmentExtend.setPageNumber(result.getAttachmentMetadata().getPageSize());
            } else {
                GetNumberPagesOfPdfResponse result = fileService.getNumberPagesOfPdf(vbAttachment.getFileServiceId());
                vbAttachmentExtend.setPageNumber(result.getNumberOfPages());
            }
        }
        // detect case create new outgoing doc - end

        return vbAttachmentExtend;
    }

    @Override
    public VbAttachment findByAttachmentId(Long attachmentId, Long objectType) {
        return vbAttachmentRepository.findByAttachmentIdAndIsDelete(attachmentId, Constants.DELETE.NORMAL);
    }

    @Override
    public List<VbAttachment> findByAttachmentIds(List<Long> attachmentIds, Long objectType) {
        return vbAttachmentRepository.findByAttachmentIdInAndIsDelete(attachmentIds, Constants.DELETE.NORMAL, objectType);
    }

    @Override
    public void savaAttachment(VbAttachment attachment) {
        vbAttachmentRepository.save(attachment);
    }

    @Override
    public List<VbAttachment> findByObjectIdAndObjectType(Long objectId, Long objectType) {
        return vbAttachmentRepository.findByObjectIdAndObjectTypeAndIsDelete(objectId, objectType, Constants.DELETE.NORMAL);
    }
    @Override
    public Boolean updateFileForObject(Long objecId , List<Long> listImageIds, Long type) throws BadRequestException {
        if(StringUtils.isTrue(listImageIds)){

            for ( Long attachId : listImageIds) {
                VbAttachment vbAttachment = vbAttachmentRepository.findByAttachmentIdAndIsDelete(attachId, Constants.DELETE.NORMAL);
                if(StringUtils.isTrue(vbAttachment)){
                    vbAttachment.setObjectId(objecId);
                    vbAttachmentRepository.save(vbAttachment);
                }
                else throw new BadRequestException("File not found or deleted");
            }
            return true;
        }
        return false;
    }

    @Override
    public VbAttachment deleteFile(Long attachmentId) {
        VbAttachment vbAttachment = vbAttachmentRepository.findByAttachmentIdAndIsDelete(attachmentId, Constants.DELETE.NORMAL);
        if(StringUtils.isTrue(vbAttachment)){
            vbAttachment.setIsDelete(Constants.DELETE.DELETED);
            vbAttachmentRepository.save(vbAttachment);
        }
        return vbAttachment;
    }


    @Override
    public VbAttachment updateFileInfo(Long attachmentId, VbAttachment vbAttachment) {
        VbAttachment vbAttachmentOld = vbAttachmentRepository.findByAttachmentIdAndIsDelete(attachmentId, Constants.DELETE.NORMAL);
        if(StringUtils.isTrue(vbAttachmentOld)){
            vbAttachmentOld.setFileName(vbAttachment.getFileName());
            vbAttachmentOld.setNote(vbAttachment.getNote());
            vbAttachmentOld.setContentType(vbAttachment.getContentType());
            vbAttachmentOld.setStorageType(vbAttachment.getStorageType());

            vbAttachmentRepository.save(vbAttachmentOld);
        }
        return vbAttachmentOld;
    }

    @Override
    public String downloadFileBase64(Long attachmentId) throws IOException {
        VbAttachment vbAttachment = vbAttachmentRepository.findByAttachmentIdAndIsDelete(attachmentId, Constants.DELETE.NORMAL);
        InputStream inputStream = fileService.getInputStream(vbAttachment.getFileServiceId(), new DownloadOption());

        //convert inputStream to base64
        String base64 = Base64.getEncoder().encodeToString(IOUtils.toByteArray(inputStream));
        return base64;
    }

}
