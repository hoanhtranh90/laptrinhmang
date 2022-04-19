/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author sadfsafbhsaid
 */
@Entity
@Table(name = "attach_relation", indexes = {
    @Index(name = "index_attach_relation", columnList = "creator_id,updator_id")})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AttachRelation extends Auditable<String> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "attach_relation_id")
    private Long attachRelationId;

    @Column(name = "OBJECT_ID")
    private Long objectId;
    
    @Column(name = "attachment_id")
    private Long attachmentId;

    @Column(name = "OBJECT_TYPE")
    private Long objectType;

    @Column(name = "attach_type")
    private Long attachType;

    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "updator_id")
    private Long updatorId;
    
    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "IS_DELETE")
    private Long isDelete = 0L;
}
