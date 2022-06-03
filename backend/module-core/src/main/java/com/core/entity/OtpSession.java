package com.core.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "otp_session")
@Setter
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class OtpSession extends Auditable<String> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otp_session_id")
    private Long otpSessionId;

    //code
    @Column(name = "code")
    private Long code;

    //time expired
    @Column(name = "time_expired")
    private Date timeExpired;

    //is_delete
    @Column(name = "is_delete")
    private Long isDelete;

    //email
    @Column(name = "email")
    private String email;
}
