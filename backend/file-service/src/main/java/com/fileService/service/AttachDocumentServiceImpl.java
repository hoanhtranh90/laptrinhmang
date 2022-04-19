package com.fileService.service;

import com.core.entity.AttachDocument;
import com.core.entity.ImageExtract;
import com.core.model.Actor;
import com.core.model.file.DownloadOption;
import com.core.model.file.GetNumberPagesOfPdfResponse;
import com.core.repository.AttachDocumentRepository;
import com.core.repository.ImageExtractRepository;
import com.fileService.base.Constants;
import com.fileService.base.H;
import com.itextpdf.text.pdf.PdfReader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AttachDocumentServiceImpl implements AttachDocumentService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AttachDocumentRepository attachDocumentRepository;
    @Autowired
    private ImageExtractRepository imageExtractRepository;
    @Autowired
    private StorageService storageService;
    @Autowired
    private DownloadService downloadService;

    @Override
    public AttachDocument findById(Long id) {
        return attachDocumentRepository.findAttachById(id);
    }

    @Override
    public AttachDocument save(AttachDocument attachDocument, Actor actor) {
        if (actor != null) {
            attachDocument.setCreatorId(actor.getUserId());
            attachDocument.setCreatedDate(new Date());
            attachDocument.setModifiedDate(new Date());
            attachDocument.setUpdatorId(actor.getUserId());
            attachDocument.setCreatedBy(actor.getFullName());
            attachDocument.setModifiedBy(actor.getFullName());
        }
        attachDocument.setIsDelete(Constants.DELETE.NORMAL);
        attachDocumentRepository.save(attachDocument);
        return attachDocument;
    }

    @Override
    public AttachDocument populateOriginalAttachDocument(File finalOriginalFile) {
        AttachDocument originalAttachDocument = new AttachDocument();
        originalAttachDocument.setFileName(finalOriginalFile.getName());
        originalAttachDocument.setFileSize(finalOriginalFile.length());
        originalAttachDocument.setIsPdf(null);
        originalAttachDocument.setIsOriginal(Constants.ATTACH_DOCUMENT.ORIGINAL);
        originalAttachDocument.setFilePath(finalOriginalFile.getAbsolutePath());
        return originalAttachDocument;
    }

    private void downloadFile_(
            AttachDocument attachDocument, DownloadOption downloadOption, HttpServletResponse response) {

        try {
            this.getPdfFile(
                    attachDocument, downloadOption,
                    inputStream -> {
                        try {
                            MediaType mediaType = H.isTrue(downloadOption.getWaterMarkText())
                            || H.isTrue(downloadOption.getIncludeAttachDocumentId())
                            ? MediaType.APPLICATION_PDF : MediaType.valueOf(attachDocument.getContentType());
                            String fileName = attachDocument.getFileName();
                            downloadService.downloadFile(response, fileName, mediaType.toString(), os -> {
                                try {
                                    storageService.writeFile(inputStream, os);
                                } catch (IOException e) {
                                    throw new StorageException("downloadFile.error", e);
                                }
                            });
                        } catch (Exception ex) {
                            throw new StorageException("downloadFile.error", ex);
                        }
                    }
            );
        } catch (Exception ex) {
            throw new StorageException("downloadFile.error", ex);
        }
    }

    private void downloadFileImage(DownloadOption downloadOption, HttpServletResponse response) {
        try {
            ImageExtract imageExtract = imageExtractRepository.findByAttachmentMetadataIdAndPageNumber(
                    downloadOption.getAttachmentMetadataId(), downloadOption.getPageNumber());
            InputStream inputStream = storageService.getInputStream(
                    new FileInputStream(imageExtract.getFilePath()),
                    Constants.ATTACH_DOCUMENT.ENCRYPTED.equals(imageExtract.getIsEncrypt())
            );
            downloadService.downloadFile(
                    response, imageExtract.getFileName(), MediaType.IMAGE_PNG.toString(),
                    os -> {
                        try {
                            storageService.writeFile(inputStream, os);
                        } catch (IOException ex) {
                            throw new StorageException("downloadFile.error", ex);
                        }
                    }
            );
        } catch (IOException ex) {
            throw new StorageException("downloadFile.error", ex);
        }
    }

    @Override
    public void downloadFile(
            HttpServletResponse response, Long id, DownloadOption downloadOption
    ) {
        AttachDocument attachDocument;

        attachDocument = findById(id);
        downloadFile_(attachDocument, downloadOption, response);
    }

    private void downloadTrangChuKy(AttachDocument attachDocument, HttpServletResponse response) {
        try {
            InputStream inputStream = storageService.getInputStream(attachDocument);
            PDDocument document = PDDocument.load(inputStream);
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 150, ImageType.RGB);
            String fileName = String.format("%s%s", UUID.randomUUID().toString(), Constants.FILE_EXTENSION.IMAGE_PNG);
            downloadService.downloadFile(response, fileName, MediaType.IMAGE_PNG.toString(), os -> {
                try {
                    ImageIO.write(bim, "PNG", os);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (IOException ex) {
            throw new StorageException("downloadTrangChuKy.error", ex);
        }
    }

    @Override
    public void getPdfFile(AttachDocument attachDocument, DownloadOption downloadOption, IProcessFile processFile) {

        List<File> tempFiles = new ArrayList<>();
        try {
            File finalFile;
            InputStream finalInputStream = storageService.getInputStream(attachDocument);

            /**
             * include includeAttachDocument to attachDocument
             */
            Long includeAttachDocumentId = downloadOption.getIncludeAttachDocumentId();
            AttachDocument includeAttachDocument = H.isTrue(includeAttachDocumentId)
                    ? findById(includeAttachDocumentId) : null;
            if (H.isTrue(includeAttachDocument)) {
                finalFile = storageService.mergePdfFiles(
                        storageService.getLocalTempFile(),
                        storageService.getInputStream(includeAttachDocument), finalInputStream
                );
                finalInputStream = new FileInputStream(finalFile);
                tempFiles.add(finalFile);
            }

            /**
             * them water-mark text;
             */
            String waterMarkText = downloadOption.getWaterMarkText();
            if (H.isTrue(waterMarkText)) {
                storageService.addWaterMark(
                        finalInputStream, waterMarkText,
                        new FileOutputStream(finalFile = storageService.getLocalTempFile())
                );
                finalInputStream = new FileInputStream(finalFile);
                tempFiles.add(finalFile);
            }

            processFile.do_(finalInputStream);
        } catch (FileNotFoundException e) {
            throw new StorageException("getPdfFile error", e);
        } finally {
            try {
                if (H.isTrue(tempFiles)) {
                    storageService.deleteFile(tempFiles);
                }
            } catch (Exception ex) {
                logger.warn("delete tempFiles error", ex);
            }
        }
    }

    @Override
    public AttachDocument populateOriginalAttachDocument(
            File finalOriginalFile, String fileId, Boolean isEncrypted, String contentType) {
        AttachDocument originalAttachDocument = new AttachDocument();
        originalAttachDocument.setFileName(finalOriginalFile.getName());
        originalAttachDocument.setFileSize(finalOriginalFile.length());
        originalAttachDocument.setIsOriginal(Constants.ATTACH_DOCUMENT.ORIGINAL);
        originalAttachDocument.setFilePath(finalOriginalFile.getAbsolutePath());
        originalAttachDocument.setIsEncrypt(isEncrypted ? Constants.ATTACH_DOCUMENT.ENCRYPTED : null);
        originalAttachDocument.setContentType(contentType);
        return originalAttachDocument;
    }

    @Override
    public GetNumberPagesOfPdfResponse getNumberPagesOfPdf(Long id) {
        try {
            AttachDocument attachDocument = findById(id);
            InputStream finalInputStream = storageService.getInputStream(attachDocument);
            PdfReader pdfReader = new PdfReader(finalInputStream);
            int numberOfPages = pdfReader.getNumberOfPages();
            GetNumberPagesOfPdfResponse result = new GetNumberPagesOfPdfResponse();
            result.setId(id);
            result.setNumberOfPages((long) numberOfPages);
            return result;
        } catch (IOException e) {
            throw new StorageException("getNumberPagesOfPdf.error", e);
        }
    }
}
