package com.core.model.account;

import com.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Date;

/**
 *
 * @author DELL
 */
@JsonInclude(value = Include.NON_NULL)
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "JSon Object Basic DTO cho User Cập nhật ")
public class UserUpdateDto {

    @NotNull(message = "Id người dùng không bỏ trống. ")
    @Min(value = 1, message = "Id người dùng không hợp lệ.")
    private Long userId;

    @Pattern(regexp = StringUtils.REGEX_PHONE, message = "Số điện thoại phải có định dạng sau (0xxx..., 84xxx...) ")
    private String phoneNumber;

    @Pattern(regexp = StringUtils.REGEX_EMAIL, message = " Địa chỉ Email không hợp lệ. ")
    private String email;

    @Pattern(regexp = StringUtils.REGEX_USERNAME, message = " Tên đăng nhập không hợp lệ. ")
    @Size(min = 3, message = " Tên đăng nhập phải từ 3 ký tự.")
    @Size(max = 25, message = " Tên đăng nhập không được vượt quá 25 ký tự.")
    private String userName;

    @Size(max = 50, message = "Họ và tên không được lớn hơn 50 ký tự.")
//    @Pattern(regexp = StringUtils.REGEX_FULLNAME, message = " Họ và tên không hợp lệ. Họ và tên lớn hơn 2 từ và không chứa số, các ký tự đặc biệt.")
    private String fullName;

    private Integer sex;


    @JsonFormat(timezone = StringUtils.TIME_ZONE)
    private Date dateOfBirth;

    @Size(max = 255, message = "Họ và tên không được lớn hơn 255 ký tự.")
    private String address;


//  @JsonProperty(value = "role_code")
//  @NotNull(message = " Quyền không được trống. ")
//  @NotEmpty(message = " Quyền không được trống. ")
//  private String roleCode
    @Min(value = 1, message = "Trạng thái không hợp lệ. Chọn 1: Actived, 2: Inactive.")
    @Max(value = 2, message = "Trạng thái không hợp lệ. Chọn 1: Actived, 2: Inactive.")
    private Integer status;


}
