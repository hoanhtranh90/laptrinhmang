/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author sadfsafbhsaid
 */
@Entity
@Table(name = "action_log", indexes = {
    @Index(name = "index_action_log", columnList = "user_id,type,action_,role_id,dept_id")})
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActionLog extends Auditable<String> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "action_log_id")
    private Long actionLogId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "dept_id")
    private Long deptId;

    @Column(name = "action_")
    private Long action;

    @Basic(optional = false)
    @NotNull
    @Column(name = "type")
    private Long type;

    @Basic(optional = false)
    @NotNull
    @Column(name = "object_id")
    private Long objectId;

    @Column(name = "ip_address", length = 255)
    private String ipAddress;

    @Column(name = "ip_mac", length = 255)
    private String ipMac;

    @Column(name = "user_agent", length = 255)
    private String userAgent;

    @Lob
    @Size(max = 2147483647)
    @Column(name = "data_json")
    private String dataJson;
}
