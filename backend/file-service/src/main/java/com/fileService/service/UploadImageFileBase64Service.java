package com.fileService.service;

import com.core.entity.AttachDocument;
import com.core.model.Actor;
import com.core.model.file.UploadOption;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.UUID;

@Service
@Scope("request")
@Transactional
public class UploadImageFileBase64Service {

    @Autowired
    private StorageService storageService;
    @Autowired
    private AttachDocumentService attachDocumentService;

    private String getSubFolder() {
        return storageService.getSubFolder();
    }

    public AttachDocument uploadImageFileBase64(String imageBase64, UploadOption uploadOption, Actor actor) throws Exception {
        String fileId = UUID.randomUUID().toString();
        String subFolder = getSubFolder();
        Base64InputStream inputStream = new Base64InputStream(IOUtils.toInputStream(imageBase64), true);
        File finalOriginalFile = storageService.saveFile(fileId, subFolder, inputStream, uploadOption.getNeedEncrypt());
        return attachDocumentService.save(attachDocumentService.populateOriginalAttachDocument(
                finalOriginalFile, fileId, Boolean.TRUE.equals(uploadOption.getNeedEncrypt()), uploadOption.getContentType()
        ), actor);
    }
}
