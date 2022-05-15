package com.core.model.account;

import com.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 *
 * @author DELL
 */
@JsonInclude(value = Include.NON_NULL)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "JSon Object Basic DTO cho User Cập nhật ")
public class UserUpdateDtoByUser {

//    @Pattern(regexp = StringUtils.REGEX_PHONE, message = "Số điện thoại phải có định dạng sau (0xxx..., 84xxx...) ")
    private String phoneNumber;

//    @Pattern(regexp = StringUtils.REGEX_EMAIL, message = " Địa chỉ Email không hợp lệ. ")
    private String email;

    @Pattern(regexp = StringUtils.REGEX_USERNAME, message = " Tên đăng nhập không hợp lệ. ")
    @Size(min = 3, message = " Tên đăng nhập phải từ 3 ký tự.")
    @Size(max = 25, message = " Tên đăng nhập không được vượt quá 25 ký tự.")
    private String userName;

    @Size(max = 50, message = "Họ và tên không được lớn hơn 50 ký tự.")
//    @Pattern(regexp = StringUtils.REGEX_FULLNAME, message = " Họ và tên không hợp lệ. Họ và tên lớn hơn 2 từ và không chứa số, các ký tự đặc biệt.")
    private String fullName;

    private String identificationNumber;
    private Integer sex;

    private Date dateOfBirth;

    @Size(max = 255, message = "Họ và tên không được lớn hơn 255 ký tự.")
    private String address;



    @Min(value = 1, message = "Trạng thái không hợp lệ. Chọn 1: Actived, 2: Inactive.")
    @Max(value = 2, message = "Trạng thái không hợp lệ. Chọn 1: Actived, 2: Inactive.")
    private Integer status;





}
