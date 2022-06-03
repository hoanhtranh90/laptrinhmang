package com.business.services;

import com.business.utilts.DateUtils;
import com.core.entity.OtpSession;
import com.core.exception.BadRequestException;
import com.core.repository.OtpSessionRepository;
import com.core.utils.H;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class OTPService {

    @Autowired
    private OtpSessionRepository otpSessionRepository;

    private static final Integer EXPIRE_MINS = 5;

    public Long generateOTP(String email){
        Random random = new Random();
        Long otp = Long.valueOf(100000 + random.nextInt(900000));


        OtpSession otpSession = new OtpSession();
        otpSession.setEmail(email);
        otpSession.setCode(otp);

        //get current date + EXPIRE_MINS
        Date newDate = DateUtils.addMinutes(new Date(), EXPIRE_MINS);
        otpSession.setTimeExpired(newDate);
        otpSessionRepository.save(otpSession);

        return otp;
    }

    public Long getOtp(String email) throws BadRequestException {
        List<OtpSession> otpSessionList = otpSessionRepository.findAllByEmail(email);
        //sort otpSession by createDate
        OtpSession otpSession = otpSessionList.get(0);

        if(!H.isTrue(otpSession)){
            return -1L;
        }
        if(otpSession.getTimeExpired().before(new Date())){
            throw new BadRequestException("OTP expired");
        }
        return otpSession.getCode();
    }

    public void clearOTP(String key){
        List<OtpSession> otpSessionList = otpSessionRepository.findAllByEmail(key);
        if(!H.isTrue(otpSessionList)){
            return;
        }
        for (OtpSession otpSession : otpSessionList) {
            otpSessionRepository.delete(otpSession);
        }
    }
}

