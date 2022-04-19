package com.core.model.account;

import com.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.*;
import java.util.Date;


/**
 * 
 * @author DELL
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "JSon Object Basic DTO cho User Đăng ký")
public class UserRegisterDto {

    private String avatar;

//    @NotNull(message = "Số điện thoại không bỏ trống. ")
//    @NotEmpty(message = "Số điện thoại không bỏ trống. ")
//    @Pattern(regexp = StringUtils.REGEX_PHONE, message = "Số điện thoại phải có định dạng sau (0xxx..., 84xxx...) ")
    private String phoneNumber;

//    @NotNull(message = "Địa chỉ Email không bỏ trống. ")
//    @NotEmpty(message = "Địa chỉ Email không bỏ trống. ")
//    @Pattern(regexp = StringUtils.REGEX_EMAIL, message = " Địa chỉ Email không hợp lệ. ")
    private String email;

    private String userName;

    @NotEmpty(message = "Họ và tên không bỏ trống. ")
    @NotNull(message = "Họ và tên không bỏ trống. ")
    @Size(max = 50, message = "Họ và tên không được lớn hơn 50 ký tự.")
    private String fullName;
    
    @JsonFormat(timezone = StringUtils.TIME_ZONE)
    private Date dateOfBirth;

    private Integer sex;

    @Size(max = 255, message = "Họ và tên không được lớn hơn 255 ký tự.")
    private String address;
    
    @Min(value = 1, message = "Trạng thái không hợp lệ. Chọn 1: Actived, 2: Inactive.")
    @Max(value = 2, message = "Trạng thái không hợp lệ. Chọn 1: Actived, 2: Inactive.")
    private Long status;

//    @NotNull
//    @NotEmpty
//    private String roleCode;
    @NotEmpty(message = "password không bỏ trống. ")
    @NotNull(message = "password không bỏ trống. ")
    private String password;
    private String identificationNumber;


}
