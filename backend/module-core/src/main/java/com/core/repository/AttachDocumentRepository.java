package com.core.repository;

import com.core.entity.AttachDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository : VbAttachment.
 */
@Repository
public interface AttachDocumentRepository extends JpaRepository<AttachDocument, Long> {
    @Query("select a from AttachDocument a where a.attachDocumentId = :id")
    AttachDocument findAttachById(long id);

}
