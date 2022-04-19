/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.repository;

import com.core.entity.ImageExtract;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author sadfsafbhsaid
 */
@Repository
public interface ImageExtractRepository extends JpaRepository<ImageExtract, Long>, JpaSpecificationExecutor<ImageExtract> {

    List<ImageExtract> findAllByAttachmentMetadataId(Long attachmentMetadataId);

    ImageExtract findByAttachmentMetadataIdAndPageNumber(Long attachmentMetadataId, Long pageNumber);

    List<ImageExtract> findAllByImageExtractIdIn(List<Long> imageExtractIds);
}
