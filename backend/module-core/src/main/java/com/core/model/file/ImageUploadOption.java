package com.core.model.file;

import java.util.List;

public class ImageUploadOption {
    private Long attachMetadataCommnetId;
    private Long attachMetadataOriginalId;
    private List<Long> imageExtractCommentIds;

    public Long getAttachMetadataCommnetId() {
        return attachMetadataCommnetId;
    }

    public void setAttachMetadataCommnetId(Long attachMetadataCommnetId) {
        this.attachMetadataCommnetId = attachMetadataCommnetId;
    }

   
    public Long getAttachMetadataOriginalId() {
        return attachMetadataOriginalId;
    }

    public void setAttachMetadataOriginalId(Long attachMetadataOriginalId) {
        this.attachMetadataOriginalId = attachMetadataOriginalId;
    }

    public List<Long> getImageExtractCommentIds() {
        return imageExtractCommentIds;
    }

    public void setImageExtractCommentIds(List<Long> imageExtractCommentIds) {
        this.imageExtractCommentIds = imageExtractCommentIds;
    }
}
