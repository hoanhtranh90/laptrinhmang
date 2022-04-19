package com.core.model.account;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

/**
 *
 * @author DELL
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "JSon Object Basic DTO cho User Tìm kiếm")
public class UserSearchDto {

    private String userName;
    
    private String roleCode;
    
    private String fullName;
    
    private String phoneNumber;
    
    private String email;


    private List<String> groupRoleActionCodelist;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = StringUtils.DF_YYYY_MM_DD)
//    private Date createdDateFrom;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = StringUtils.DF_YYYY_MM_DD)
//    private Date createdDateTo;

    private Long deptId;
}
