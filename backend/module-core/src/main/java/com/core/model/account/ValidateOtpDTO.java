package com.core.model.account;


import lombok.Data;

@Data
public class ValidateOtpDTO {
    private int otp;
    private String email;
}
