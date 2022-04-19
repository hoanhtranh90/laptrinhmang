package com.fileService.service;

import com.core.entity.ImageExtract;
import com.core.model.file.UploadOption;
import com.fileService.base.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Scope("request")
@Transactional
public class UploadImageVanBanService {

    @Autowired
    private StorageService storageService;
    @Autowired
    private ImageExtractService imageExtractService;

    private String getSubFolder() {
        return storageService.getSubFolder();
    }

    public ImageExtract uploadImageVanBan(UploadOption uploadOption, MultipartFile uploadFile) throws IOException {
        String imageFileId = UUID.randomUUID().toString();
        File finalOriginalFile = storageService.saveFile(
                imageFileId, getSubFolder(), uploadFile.getInputStream(), uploadOption.getNeedEncrypt()
        );
        ImageExtract imageExtract = new ImageExtract();
        imageExtract.setFileSize(finalOriginalFile.getTotalSpace());
        imageExtract.setPageNumber(uploadOption.getPageNumber());
        imageExtract.setFileName(finalOriginalFile.getName());
        imageExtract.setFileExtention(Constants.FILE_EXTENSION.IMAGE_PNG);
        imageExtract.setFilePath(finalOriginalFile.getAbsolutePath());
        imageExtract.setNote(imageFileId);
        imageExtract.setIsEncrypt(uploadOption.getNeedEncrypt() ? Constants.ATTACH_DOCUMENT.ENCRYPTED : null);
        imageExtract.setStatus(Constants.STATUS.ACTIVE);
        imageExtractService.save(imageExtract);
        return imageExtract;
    }
}
