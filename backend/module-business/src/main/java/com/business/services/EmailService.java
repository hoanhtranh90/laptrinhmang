package com.business.services;



import com.core.entity.User;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface EmailService {

    void sendEmail(String subject, String content, List<String> recipents) throws MessagingException;
    String buildEmailSendOtpForResetPassWord(User user, Long otp);
}
