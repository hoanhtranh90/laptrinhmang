package com.core.repository;

import com.core.entity.OtpSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OtpSessionRepository extends JpaRepository<OtpSession, Long> {
    @Query(value = "select otp from OtpSession otp where otp.email =:email order by otp.createdDate desc ")
    List<OtpSession> findAllByEmail(String email);
}
