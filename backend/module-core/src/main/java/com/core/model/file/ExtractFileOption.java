package com.core.model.file;

import com.core.model.Actor;

public class ExtractFileOption {

    private Boolean needEncrypt;
    private Long attachmentId;
    private Long vbAttachmentId;
    private String contentType;
    private Long objectId;
    private String objectType;
    private Actor actor; 

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }
        
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Boolean getNeedEncrypt() {
        return needEncrypt;
    }

    public void setNeedEncrypt(Boolean needEncrypt) {
        this.needEncrypt = needEncrypt;
    }

    public Long getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Long attachmentId) {
        this.attachmentId = attachmentId;
    }

    public Long getVbAttachmentId() {
        return vbAttachmentId;
    }

    public void setVbAttachmentId(Long vbAttachmentId) {
        this.vbAttachmentId = vbAttachmentId;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

        public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }
}
