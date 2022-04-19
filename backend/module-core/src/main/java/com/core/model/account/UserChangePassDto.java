package com.core.model.account;

import lombok.Data;

@Data
public class UserChangePassDto {
    private String oldPass;
    private String newPass;
    private String confirmPass;
}
