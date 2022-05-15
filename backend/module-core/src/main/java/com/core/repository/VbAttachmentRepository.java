/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.repository;

import com.core.entity.VbAttachment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author sadfsafbhsaid
 */
@Repository
public interface VbAttachmentRepository extends JpaRepository<VbAttachment, Long> {

    @Query("select a from VbAttachment a " +
            "where a.isDelete = 0 " +
            "and a.objectId = :objectId " +
            "and (:objectType is null or a.objectType = :objectType)")
    List<VbAttachment> getAllByObject(Long objectId, Long objectType);

    @Query("select a from VbAttachment a where a.isDelete =:isDelete and a.attachmentId = :attachmentId ")
    VbAttachment findByAttachmentIdAndIsDelete(Long attachmentId, Long isDelete);

    @Query("select a from VbAttachment a where a.isDelete =:isDelete and a.attachmentId in (:attachmentIds) and (:objectType is null or a.objectType = :objectType) ")
    List<VbAttachment> findByAttachmentIdInAndIsDelete(List<Long> attachmentIds, Long isDelete, Long objectType);

    @Query("select a from VbAttachment a where a.isDelete =:isDelete and a.objectId =:objectId and  a.objectType = :objectType order by a.createdDate desc")
    List<VbAttachment> findByObjectIdAndObjectTypeAndIsDelete(Long objectId, Long objectType, Long isDelete);

    @Query("select a from VbAttachment a where a.isDelete =:isDelete and a.attachmentId = :attachmentId ")
    VbAttachment findByIdAndIsDelete(Long attachmentId, Long isDelete);
}
