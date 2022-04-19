package com.core.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ATTACHMENT_METADATA")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AttachmentMetadata implements Serializable{
private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_meta_id", nullable = false)
    private Long attachmentMetaId;
    
    @Column(name = "VB_ATTACHMENT_ID", nullable = true)
    private Long vbAttachmentId;
    
    @Basic
    @Column(name = "OBJECT_TYPE", nullable = true, length = 100)
    private String objectType;
    
    @Column(name = "SIZE_TOTAL", nullable = true)
    private Long sizeTotal;
    
    @Column(name = "PAGE_SIZE", nullable = true)
    private Long pageSize;
    
    @Column(name = "STATUS", nullable = true)
    private String status;
    @Column(name = "IS_DELETE", nullable = true, columnDefinition = "bigint default 0")
    private Long isDelete = 0L;
    
    @Column(name = "CREATOR_ID")
    private Long creatorId;

    @Column(name = "UPDATOR_ID")
    private String updatorId;

}
