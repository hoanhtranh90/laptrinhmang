package com.fileService.service;

import com.core.entity.AttachDocument;
import com.core.model.Actor;
import com.core.model.file.UploadOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@Scope("request")
@Transactional
public class UploadFileService {

    @Autowired
    private StorageService storageService;
    @Autowired
    private AttachDocumentService attachDocumentService;

    private UploadOption uploadOption;
    private InputStream inputStream;

    public UploadOption getUploadOption() {
        return uploadOption;
    }

    public void setUploadOption(UploadOption uploadOption) {
        this.uploadOption = uploadOption;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public AttachDocument uploadFile(UploadOption uploadOption, MultipartFile uploadFile,Actor actor) throws IOException {
        this.uploadOption = uploadOption;
        inputStream = uploadFile.getInputStream();
        return this.saveFileWithoutPdf(actor);
    }

    private String getSubFolder() {
        return storageService.getSubFolder();
    }

    public AttachDocument saveFileWithoutPdf(Actor actor) {
        String contentType = uploadOption.getContentType();
        String pdfFileId = UUID.randomUUID().toString();
        String subFolder = getSubFolder();
        File finalOriginalFile = storageService.saveFile(pdfFileId, subFolder, inputStream, uploadOption.getNeedEncrypt());
        return attachDocumentService.save(attachDocumentService.populateOriginalAttachDocument(
                finalOriginalFile, pdfFileId, Boolean.TRUE.equals(uploadOption.getNeedEncrypt()), contentType
        ),actor);
    }
}
