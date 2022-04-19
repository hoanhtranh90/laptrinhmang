package com.fileService.service;

import com.core.entity.ImageExtract;
import com.core.model.file.ImageUploadOption;
import com.fileService.base.H;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Scope("request")
@Transactional
public class UploadCommentFileService {

    @Autowired
    private ImageExtractService imageExtractService;

    private ImageExtract getImageChange(ImageExtract imageExtract, List<ImageExtract> imageExtractComments) {
        return H.find(imageExtractComments, (index, item) -> item.getPageNumber().equals(imageExtract.getPageNumber()));
    }

    public void uploadCommentFile(ImageUploadOption imageUploadOption) {

        Long attachMetadataCommnetId = imageUploadOption.getAttachMetadataCommnetId();
        Long attachMetadataOriginalId = imageUploadOption.getAttachMetadataOriginalId();
        List<Long> imageExtractCommentIds = imageUploadOption.getImageExtractCommentIds();

        List<ImageExtract> imageExtractComment = imageExtractService.findAllByIdIn(imageExtractCommentIds);
        List<ImageExtract> imageExtractsOrigin = imageExtractService.findAllByAttachmentMetadataId(attachMetadataOriginalId);
        for (ImageExtract imageExtract : imageExtractsOrigin) {
            ImageExtract imageExtractClone = new ImageExtract(imageExtract);
            imageExtractClone.setAttachmentMetadataId(attachMetadataCommnetId);
            ImageExtract imageExtractEdited = getImageChange(imageExtract, imageExtractComment);
            if (imageExtractEdited != null) {
                imageExtractClone.setIsEdited(1L);
                imageExtractClone.setFileName(imageExtractEdited.getFileName());
                imageExtractClone.setFilePath(imageExtractEdited.getFilePath());
            }
            imageExtractService.save(imageExtractClone);
        }
    }
}
