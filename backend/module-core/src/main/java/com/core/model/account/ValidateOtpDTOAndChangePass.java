package com.core.model.account;


import lombok.Data;

@Data
public class ValidateOtpDTOAndChangePass {
    private int otp;
    private String email;

    private String newPassword;
    private String confirmPassword;
}
