package com.fileService.service;


import com.core.entity.ImageExtract;
import java.util.List;

public interface ImageExtractService {
    List<ImageExtract> findAllByIdIn(List<Long> ids);

    void save(ImageExtract imageExtract);

    List<ImageExtract> findAllByAttachmentMetadataId(Long attachmentMetadataId);
}
