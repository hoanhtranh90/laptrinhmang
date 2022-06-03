package com.business.services.impl;

import com.business.services.EmailService;

import com.core.entity.User;
import com.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender emailSender;
    @Value("${domain.name}")
    private String domain;


    @Override
    public void sendEmail(String subject, String content, List<String> recipents) throws MessagingException {
        boolean isHtml = true;
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        String[] recipentsArr = new String[recipents.size()];
        recipents.toArray(recipentsArr);
        helper.setTo(recipentsArr);
        helper.setSubject(subject);
        helper.setText(content, isHtml);

        emailSender.send(message);
    }
    @Override
    public String buildEmailSendOtpForResetPassWord(User user, Long otp) {
        String emailTemplate = "SendOTP.html";
        return StringUtils.renderEmail(emailTemplate, populateEmailModelForResetPassword(new HashMap<>(), user, otp));

    }

    private Map<String, Object> populateEmailModelForResetPassword(HashMap<String, Object> data, User user, Long otpNumBer) {
        data.put("user", user.getFullName());
        data.put("otpNumBer", otpNumBer);
        return data;
    }



}
