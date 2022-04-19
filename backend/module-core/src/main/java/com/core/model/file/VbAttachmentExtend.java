/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.model.file;

import com.core.entity.VbAttachment;

/**
 *
 * @author sadfsafbhsaid
 */
public class VbAttachmentExtend extends VbAttachment {

    private Long pageNumber;

    public VbAttachmentExtend() {
        super();
    }

    public VbAttachmentExtend(VbAttachment vbAttachment) {

        this.setAttachmentId(vbAttachment.getAttachmentId());
        this.setObjectId(vbAttachment.getObjectId());
        this.setObjectType(vbAttachment.getObjectType());
        this.setFileName(vbAttachment.getFileName());
        this.setFileExtention(vbAttachment.getFileExtention());
        this.setFilePath(vbAttachment.getFilePath());
        this.setNote(vbAttachment.getNote());
        this.setIsSigned(vbAttachment.getIsSigned());
        this.setIsEncrypt(vbAttachment.getIsEncrypt());
        this.setIsDelete(vbAttachment.getIsDelete());
        this.setCreatorId(vbAttachment.getCreatorId());
        this.setUpdatorId(vbAttachment.getUpdatorId());
        this.setSignedDocId(vbAttachment.getSignedDocId());
        this.setFileServiceId(vbAttachment.getFileServiceId());
        this.setStorageType(vbAttachment.getStorageType());
        this.setContentType(vbAttachment.getContentType());
        this.setPdfAlready(vbAttachment.getPdfAlready());
    }

    public Long getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Long pageNumber) {
        this.pageNumber = pageNumber;
    }
}
