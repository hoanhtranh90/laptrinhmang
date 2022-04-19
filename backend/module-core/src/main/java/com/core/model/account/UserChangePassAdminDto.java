package com.core.model.account;

import lombok.Data;

@Data
public class UserChangePassAdminDto {
    private Long userId;
    private String newPass;
    private String confirmPass;
}
