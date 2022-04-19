/*
 * Created on 17 Dec 2018 ( Time 16:17:24 )
 * Generated by Telosys Tools Generator ( version 2.1.1 )
 */
// This Bean has a basic Primary Key (not composite) 
package com.core.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

//import javax.validation.constraints.* ;
//import org.hibernate.validator.constraints.* ;

/**
 * Persistent class for entity stored in table "VB_ATTACHMENT"
 *
 * @author admin
 */
@Entity
@Table(name = "VB_ATTACHMENT")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class VbAttachment extends Auditable<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    //----------------------------------------------------------------------
    // ENTITY PRIMARY KEY ( BASED ON A SINGLE FIELD )
    //----------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ATTACHMENT_ID", nullable = false)
    private Long attachmentId;

    //----------------------------------------------------------------------
    // ENTITY DATA FIELDS 
    //----------------------------------------------------------------------    
    @Column(name = "OBJECT_ID")
    private Long objectId;
    
    @Column(name = "type_user",columnDefinition = "bigint default 1")
    private Long typeUser;

    @Column(name = "OBJECT_TYPE")
    private Long objectType;

    @Column(name = "FILE_NAME", length = 150)
    private String fileName;

    @Column(name = "FILE_EXTENTION", length = 50)
    private String fileExtention;

    @Column(name = "FILE_PATH", length = 1000)
    private String filePath;

    @Column(name = "NOTE", length = 500)
    private String note;

    @Column(name = "IS_SIGNED")
    private Long isSigned;

    @Column(name = "IS_ENCRYPT")
    private Long isEncrypt;

    @Column(name = "IS_DELETE", columnDefinition = "bigint default 0")
    private Long isDelete = 0L;

    @Column(name = "CREATOR_ID")
    private Long creatorId;

    @Column(name = "UPDATOR_ID")
    private Long updatorId;

    @Column(name = "SIGNED_DOC_ID", length = 100)
    private String signedDocId;

    @Column(name = "FILE_SERVICE_ID")
    private Long fileServiceId;

    @Column(name = "STORAGE_TYPE", length = 100)
    private String storageType;

    @Column(name = "CONTENT_TYPE", length = 100)
    private String contentType;

    @Column(name = "PDF_ALREADY")
    private Long pdfAlready;

}