/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.business.services.impl;

import com.business.services.FileService;
import com.business.utilts.FileUtils;
import com.core.entity.AttachDocument;
import com.core.entity.VbAttachment;
import com.core.model.Actor;
import com.core.model.file.DownloadOption;
import com.core.model.file.ExtractFileResult;
import com.core.model.file.GetNumberPagesOfPdfResponse;
import com.core.model.file.UploadOption;
import com.core.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Collections;
import java.util.UUID;

/**
 *
 * @author sadfsafbhsaid
 */
@Service
@Transactional
public class FileServiceImpl implements FileService {

    @Value("${storage.tempFolderPath}")
    private String tempFolderPath;

    @Value("${fileService.uploadURL}")
    private String uploadURL;

    @Value("${fileService.downloadURL}")
    private String downloadURL;

    @Value("${fileService.convertPdfToImageURL}")
    private String convertPdfToImageURL;

    @Value("${fileService.attachDocument.getNumberPagesOfPdfURL}")
    private String getNumberPagesOfPdfURL;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public AttachDocument uploadFile(MultipartFile multipartFile, UploadOption uploadOption, Actor actor) {

        InputStream inputStream;
        String contentType = multipartFile.getContentType();
        try {
            inputStream = multipartFile.getInputStream();
            return this.uploadFile(inputStream, contentType, uploadOption,actor);
        } catch (IOException e) {
            throw new RuntimeException("fileService.uploadFail", e);
        }
    }

    public AttachDocument uploadFile(InputStream inputStream, String contentType, UploadOption uploadOption,Actor actor) {

        File tempFile = null;

        try {
            tempFile = new File(tempFolderPath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            tempFile = new File(tempFolderPath + "/" + UUID.randomUUID().toString());

            writeFile(inputStream, new FileOutputStream(tempFile));
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("needEncrypt", uploadOption.getNeedEncrypt());
            body.add("needConvertPdf", uploadOption.getNeedConvertPdf());
            body.add("file", new FileSystemResource(tempFile));
            body.add("contentType", contentType);
            body.add("actor", actor);

            return mapper.convertValue(requestPostMaping(uploadURL, body), AttachDocument.class);
        } catch (Exception e) {
            throw new RuntimeException("fileService.uploadFail", e);
        } finally {
            try {
                tempFile.delete();
            } catch (Exception ex) {
            }
        }
    }

    public Object requestPostMaping(String url, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);
        ResponseEntity<?> resultMapping
                = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Object.class);
        return resultMapping.getBody();
//        return null;
    }

    private void writeFile(InputStream inputStream, OutputStream os) throws IOException {
        FileUtils.writeFile(inputStream, os);
    }

    @Override
    public ExtractFileResult convertPdfToImage(VbAttachment vbAttachment, Actor actor) {
        /*try {

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("vbAttachmentId", vbAttachment.getAttachmentId());
            body.add("attachmentId", vbAttachment.getFileServiceId());
            body.add("contentType", vbAttachment.getContentType());
            body.add("objectId", vbAttachment.getObjectId());
            body.add("objectType", vbAttachment.getObjectType().toString());
            body.add("actor", actor);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

//            ResponseEntity<List<ImageExtract>> response =
//                    restTemplate.exchange(convertPdfToImageURL, HttpMethod.POST, requestEntity,
//                            new ParameterizedTypeReference<List<ImageExtract>>() {
//                            });
            return restTemplate.postForEntity(convertPdfToImageURL, requestEntity, ExtractFileResult.class).getBody();
        } catch (Exception e) {
            throw new RuntimeException("fileService.convertFail", e);
        } finally {

        }*/
        return null;
    }

    /**
     *
     * @param fileServiceId
     * @return
     */
    @Override
    public GetNumberPagesOfPdfResponse getNumberPagesOfPdf(Long fileServiceId) {
        /*Map<String, Object> params = new HashMap<>();
        params.put("id", fileServiceId);
        return restTemplate.getForObject(getNumberPagesOfPdfURL, GetNumberPagesOfPdfResponse.class, params);*/
        return null;
    }

    @Override
    public InputStream getInputStream(Long fileServiceId, DownloadOption downloadOption) {

        try {

            HttpClient httpClient = HttpClients.createDefault();

            URIBuilder uriBuilder = new URIBuilder(downloadURL + "/" + fileServiceId);
            uriBuilder
                    .setParameter("type", downloadOption.getType())
                    .setParameter("waterMarkText", downloadOption.getWaterMarkText())
                    .setParameter("includeAttachDocumentId", StringUtils.isTrue(downloadOption.getIncludeAttachDocumentId()) ? downloadOption.getIncludeAttachDocumentId().toString() : null)
                    .setParameter("pageNumber", StringUtils.isTrue(downloadOption.getPageNumber())
                            ? downloadOption.getPageNumber().toString() : null);

            HttpGet httpGet = new HttpGet(uriBuilder.build());
            HttpResponse response = httpClient.execute(httpGet);

            return response.getEntity().getContent();
        } catch (Exception e) {
            throw new RuntimeException("fileService.downloadFail", e);
        }
    }

}
