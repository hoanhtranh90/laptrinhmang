package com.core.model.file;


import com.core.entity.AttachmentMetadata;
import com.core.entity.ImageExtract;

import java.util.List;

public class ExtractFileResult {
    private List<ImageExtract> imageExtractList;
    private AttachmentMetadata attachmentMetadata;

    public List<ImageExtract> getImageExtractList() {
        return imageExtractList;
    }

    public void setImageExtractList(List<ImageExtract> imageExtractList) {
        this.imageExtractList = imageExtractList;
    }

    public AttachmentMetadata getAttachmentMetadata() {
        return attachmentMetadata;
    }

    public void setAttachmentMetadata(AttachmentMetadata attachmentMetadata) {
        this.attachmentMetadata = attachmentMetadata;
    }
}
