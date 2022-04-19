package com.fileService.service;

import com.core.entity.ImageExtract;
import com.core.repository.ImageExtractRepository;
import com.fileService.base.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ImageExtractServiceImpl implements ImageExtractService {

    @Autowired
    private ImageExtractRepository imageExtractRepository;

    @Override
    public List<ImageExtract> findAllByIdIn(List<Long> ids) {
        return imageExtractRepository.findAllByImageExtractIdIn(ids);
    }

    @Override
    public void save(ImageExtract imageExtract) {
        imageExtract.setIsDelete(Constants.DELETE.NORMAL);
        imageExtractRepository.save(imageExtract);
    }

    @Override
    public List<ImageExtract> findAllByAttachmentMetadataId(Long attachmentMetadataId) {
        return imageExtractRepository.findAllByAttachmentMetadataId(attachmentMetadataId);
    }
}
