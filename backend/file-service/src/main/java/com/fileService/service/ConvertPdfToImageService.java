package com.fileService.service;

import com.core.entity.AttachDocument;
import com.core.entity.AttachmentMetadata;
import com.core.entity.ImageExtract;
import com.core.model.Actor;
import com.core.model.file.ExtractFileOption;
import com.core.model.file.ExtractFileResult;
import com.fileService.base.Constants;
import com.fileService.base.H;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Scope("request")
@Transactional
public class ConvertPdfToImageService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StorageService storageService;
    @Autowired
    private AttachDocumentService attachDocumentService;
    @Autowired
    private ImageExtractService imageExtractService;

    @Value("${upload.tempSubFolder}")
    private String tempSubFolder;

    private List<File> tempFiles = new ArrayList<>();
    private ExtractFileOption extractFileOption;

    private String getSubFolder() {
        return storageService.getSubFolder();
    }

    public ExtractFileResult convertPdfToImage(ExtractFileOption extractFileOption, Actor actor) throws FileNotFoundException {
        this.extractFileOption = extractFileOption;
        AttachDocument attachDocument = attachDocumentService.findById(extractFileOption.getAttachmentId());
        if (!H.isTrue(attachDocument) || !attachDocument.getContentType().equalsIgnoreCase(Constants.CONTENT_TYPE.PDF)) {
            return null;
        }
        return extractPdfToImage(attachDocument, extractFileOption, actor);
    }

    private ExtractFileResult extractPdfToImage(
            AttachDocument pdfAttachDocument, ExtractFileOption extractFileOption, Actor actor) throws FileNotFoundException {

        ExtractFileResult extractFileResult = new ExtractFileResult();
        InputStream pdfFileInputStream = storageService.getInputStream(
                new FileInputStream(pdfAttachDocument.getFilePath()), H.isTrue(pdfAttachDocument.getIsEncrypt())
        );

        List<ImageExtract> imageExtractList = new ArrayList<>();
        PDDocument document = null;
        try {
            document = PDDocument.load(pdfFileInputStream);
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            AttachmentMetadata attachmentMetadata = populateAttachmentMetadata(
                    (long) document.getNumberOfPages(), pdfAttachDocument.getFileSize()
            );
            extractFileResult.setAttachmentMetadata(attachmentMetadata);

            for (int page = 0; page < document.getNumberOfPages(); ++page) {

                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 150, ImageType.RGB);
                File imageFile = storageService.getLocalFile(UUID.randomUUID().toString() + Constants.FILE_EXTENSION.IMAGE_PNG, tempSubFolder);

                ImageIO.write(bim, "PNG", new FileOutputStream(imageFile));
                tempFiles.add(imageFile);

                String finalImageFileID = UUID.randomUUID().toString();
                File finalImageFile = storageService.saveFile(
                        finalImageFileID, getSubFolder(), new FileInputStream(imageFile),
                        H.isTrue(pdfAttachDocument.getIsEncrypt())
                );

                imageExtractList.add(populateImageExtract(
                        pdfAttachDocument, attachmentMetadata.getAttachmentMetaId(), finalImageFile, page,
                        extractFileOption.getVbAttachmentId(), actor
                ));
            }
            document.close();

            if (H.isTrue(tempFiles)) {
                storageService.deleteFile(tempFiles);
            }

        } catch (IOException e) {
            logger.warn("extractPdfToImage: error", e);
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
            } catch (Exception ex) {
                logger.warn("document close error", ex);
            }
        }
        extractFileResult.setImageExtractList(imageExtractList);
        return extractFileResult;
    }

    private ImageExtract populateImageExtract(
            AttachDocument pdfAttachDocument, Long id, File imageConvert, Integer pageNumber, Long vbAttachmentId, Actor actor) {
        ImageExtract imageExtract = new ImageExtract();
        if (actor != null) {
            imageExtract.setCreatorId(actor.getUserId());
            imageExtract.setUpdatorId(actor.getUserId());
            imageExtract.setCreatedBy(actor.getFullName());
            imageExtract.setModifiedBy(actor.getFullName());
        }
        imageExtract.setVbAttachmentId(vbAttachmentId);
        imageExtract.setAttachmentMetadataId(id);
        imageExtract.setFileSize(imageConvert.getTotalSpace());
        imageExtract.setPageNumber(pageNumber.longValue());
        imageExtract.setFileName(imageConvert.getName());
        imageExtract.setFileExtention(Constants.FILE_EXTENSION.IMAGE_PNG);
        imageExtract.setFilePath(imageConvert.getPath());
        imageExtract.setNote("");
        imageExtract.setIsEncrypt(pdfAttachDocument.getIsEncrypt());
        imageExtract.setStatus(Constants.STATUS.ACTIVE);
        imageExtractService.save(imageExtract);
        return imageExtract;
    }

    /**
     * @param numberOfPages
     * @return
     */
    private AttachmentMetadata populateAttachmentMetadata(Long numberOfPages, Long fileSize) {
        AttachmentMetadata attachmentMetadata = new AttachmentMetadata();
        attachmentMetadata.setVbAttachmentId(extractFileOption.getVbAttachmentId());
        attachmentMetadata.setObjectType(extractFileOption.getObjectType());
        attachmentMetadata.setSizeTotal(fileSize);
        attachmentMetadata.setStatus(Constants.IMAGE_EXTRACT_STATUS.ORIGINAL);
        attachmentMetadata.setPageSize(numberOfPages);
        return attachmentMetadata;
    }
}
