package com.business.Controller;

import com.business.authencation.JwtAuthenticationResponse;
import com.business.authencation.JwtTokenProvider;
import com.business.services.UserSessionService;
import com.business.utilts.ApplicationUtils;
import com.core.config.ApplicationConfig.MessageSourceVi;
import com.core.constants.PermissionEnum;
import com.core.entity.User;
import com.core.exception.BadRequestException;
import com.core.exception.PermissionException;
import com.core.exception.UnauthorizedException;
import com.core.model.ResponseBody;
import com.core.model.account.*;
import com.core.utils.StringUtils;
import com.business.services.AcountManagementService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sangnk
 */
@RestController
@RequestMapping("/account-managers")
@Api(basePath = "/account-managers", description = "This API for account management.")
@Validated
@Slf4j
public class AcountManagementAPI {

    @Autowired
    @Qualifier("AcountManagementService_Main")
    private AcountManagementService acountManagement;

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private MessageSourceVi messageSourceVi;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    private final String OK002 = "OK002";

    private List<String> allRole;

    public AcountManagementAPI() {
        allRole = new ArrayList<>();
        allRole.add(PermissionEnum.ADMIN.getRoleCode());
    }

    /**
     * (Account Management Page) API Get Information A User.
     *
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(notes = "API sẽ được gọi trong trường hợp load thông tin 1 user để sửa tài khoản",
            value = "(Account Management Page) API Get Information A User ",
            authorizations = {
                @Authorization(value = StringUtils.API_KEY)})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Data Response Retrieved.",
                response = UserUpdateDto.class),
        @ApiResponse(code = 500, message = "Internal Server Error."),
        @ApiResponse(code = 400, message = "Bad Request cause data input."),
        @ApiResponse(code = 404, message = "Not found."),
        @ApiResponse(code = 403, message = "Access Denied Or Any More."),
        @ApiResponse(code = 401, message = "Unauthorized.")})
    public ResponseEntity<?> getUserbyId(
            @ApiParam(value = "User Id", defaultValue = "0") @PathVariable(value = "userId",
                    required = true) Long userId) {

        return ResponseEntity.ok(ResponseBody.builder().message(messageSourceVi.getMessageVi(OK002))
                .body(acountManagement.getUserById(userId)).build());
    }



    //change pass
    @PostMapping("/change-pass-by-admin")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(notes = "API sẽ được gọi trong trường hợp thay đổi mật khẩu",
            value = "(Account Management Page) API Get Information A User ",
            authorizations = {
                @Authorization(value = StringUtils.API_KEY)})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Data Response Retrieved.",
                response = UserUpdateDto.class),
        @ApiResponse(code = 500, message = "Internal Server Error."),
        @ApiResponse(code = 400, message = "Bad Request cause data input."),
        @ApiResponse(code = 404, message = "Not found."),
        @ApiResponse(code = 403, message = "Access Denied Or Any More."),
        @ApiResponse(code = 401, message = "Unauthorized.")})
    public ResponseEntity<?> changePass(
            @ApiParam(value = "User Id", defaultValue = "0") @RequestBody UserChangePassAdminDto userChangePassDto) throws BadRequestException, PermissionException {

        ApplicationUtils.checkHasRole(PermissionEnum.ADMIN.getRoleCode());
        return ResponseEntity.ok(ResponseBody.builder().message(messageSourceVi.getMessageVi(OK002))
                .body(acountManagement.changePass(userChangePassDto)).build());
    }

    //change pass
    @PostMapping("/change-pass")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(notes = "API sẽ được gọi trong trường hợp thay đổi mật khẩu",
            value = "(Account Management Page) API Get Information A User ",
            authorizations = {
                    @Authorization(value = StringUtils.API_KEY)})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data Response Retrieved.",
                    response = UserUpdateDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error."),
            @ApiResponse(code = 400, message = "Bad Request cause data input."),
            @ApiResponse(code = 404, message = "Not found."),
            @ApiResponse(code = 403, message = "Access Denied Or Any More."),
            @ApiResponse(code = 401, message = "Unauthorized.")})
    public ResponseEntity<?> changePass_(
            @ApiParam(value = "User Id", defaultValue = "0") @RequestBody UserChangePassDto userChangePassDto) throws BadRequestException, PermissionException {
        return ResponseEntity.ok(ResponseBody.builder().message(messageSourceVi.getMessageVi(OK002))
                .body(acountManagement.changePass_(userChangePassDto)).build());
    }


    @GetMapping("/profile")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(notes = "API sẽ được gọi trong trường hợp load page profile",
            value = "(Account Management Page) API Get Information Profile ",
            authorizations = {
                @Authorization(value = StringUtils.API_KEY)})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Data Response Retrieved.",
                response = UserUpdateDto.class),
        @ApiResponse(code = 500, message = "Internal Server Error."),
        @ApiResponse(code = 400, message = "Bad Request cause data input."),
        @ApiResponse(code = 404, message = "Not found."),
        @ApiResponse(code = 403, message = "Access Denied Or Any More."),
        @ApiResponse(code = 401, message = "Unauthorized.")})
    public ResponseEntity<?> getProfile() {

        return ResponseEntity.ok(ResponseBody.builder().message(messageSourceVi.getMessageVi(OK002))
                .body(acountManagement.getUserById(ApplicationUtils.getCurrentUser().getUserId())).build());
    }

    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(notes = "API sẽ được gọi trong trường hợp tạo mới account ",
            value = "(Account Management Page) API Register Account",
            authorizations = {
                @Authorization(value = StringUtils.API_KEY)})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Data Response Retrieved.", response = UserBasicDto.class),
        @ApiResponse(code = 500, message = "Internal Server Error."),
        @ApiResponse(code = 400, message = "Bad Request cause data input."),
        @ApiResponse(code = 404, message = "Not found."),
        @ApiResponse(code = 403, message = "Access Denied Or Any More."),
        @ApiResponse(code = 401, message = "Unauthorized.")})
    public synchronized ResponseEntity<?> create(
            @ApiParam(value = "JSon Object để tạo tài khoản") @Valid @RequestBody UserRegisterDto userDto)
            throws BadRequestException, PermissionException {
//        ApplicationUtils.checkHasRole(PermissionEnum.ADMIN.getRoleCode());
        return ResponseEntity
                .ok(ResponseBody.builder().body(acountManagement.createNewAccount(userDto)).message(messageSourceVi.getMessageVi("OK001")).build());

    }

    @GetMapping("/test")
    public ResponseEntity<?> getss() {
        return ResponseEntity.ok("hello");
    }

    @PutMapping("/update-profile-by-admin")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(notes = "API sẽ được gọi trong trường hợp update account by admin",
            value = "(Account Management Page) API Update Account by Admin",
            authorizations = {
                @Authorization(value = StringUtils.API_KEY)})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Data Response Retrieved.", response = UserBasicDto.class),
        @ApiResponse(code = 500, message = "Internal Server Error."),
        @ApiResponse(code = 400, message = "Bad Request cause data input."),
        @ApiResponse(code = 404, message = "Not found."),
        @ApiResponse(code = 403, message = "Access Denied Or Any More."),
        @ApiResponse(code = 401, message = "Unauthorized.")})
    @Transactional
    public synchronized ResponseEntity<?> update(@ApiParam(
            value = "JSon Object để cập nhật tài khoản") @Valid @RequestBody UserUpdateDto userUpdateDto)
            throws PermissionException, BadRequestException {

        ApplicationUtils.checkHasRole(PermissionEnum.ADMIN.getRoleCode());
        return ResponseEntity
                .ok(ResponseBody.builder().body(acountManagement.updateAccount(userUpdateDto)).message(messageSourceVi.getMessageVi("OK007")).build());
    }

    @PutMapping("/update-profile")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(notes = "API sẽ được gọi trong trường hợp update account by User",
            value = "(Account Management Page) API Update Account by User",
            authorizations = {
                    @Authorization(value = StringUtils.API_KEY)})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data Response Retrieved.", response = UserBasicDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error."),
            @ApiResponse(code = 400, message = "Bad Request cause data input."),
            @ApiResponse(code = 404, message = "Not found."),
            @ApiResponse(code = 403, message = "Access Denied Or Any More."),
            @ApiResponse(code = 401, message = "Unauthorized.")})
    @Transactional
    public synchronized ResponseEntity<?> updateByUser(@ApiParam(
            value = "JSon Object để cập nhật tài khoản") @Valid @RequestBody UserUpdateDtoByUser userUpdateDto)
            throws PermissionException, BadRequestException {

        return ResponseEntity
                .ok(ResponseBody.builder().body(acountManagement.updateAccountByUser(userUpdateDto)).message(messageSourceVi.getMessageVi("OK007")).build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(notes = "API sẽ được gọi trong trường hợp delete account ",
            value = "(Account Management Page) API Delete Account",
            authorizations = {
                @Authorization(value = StringUtils.API_KEY)})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Data Response Retrieved.", response = String.class),
        @ApiResponse(code = 500, message = "Internal Server Error."),
        @ApiResponse(code = 400, message = "Bad Request cause data input."),
        @ApiResponse(code = 404, message = "Not found."),
        @ApiResponse(code = 403, message = "Access Denied Or Any More."),
        @ApiResponse(code = 401, message = "Unauthorized.")})
    public ResponseEntity<?> deleteAccount(
            @PathVariable Long id)
            throws PermissionException {

        ApplicationUtils.checkHasRole(PermissionEnum.ADMIN.getRoleCode());
        User user = acountManagement.deleteAccount(id);

        return ResponseEntity
                .ok(ResponseBody.builder().body(user).message(messageSourceVi.getMessageVi("OK003")).build());
    }

    @PostMapping("/login")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(notes = "API sẽ được gọi trong trường hợp login ",
            value = "(Account Management Page) API Login")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Data Response Retrieved.",
                response = JwtAuthenticationResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error."),
        @ApiResponse(code = 400, message = "Bad Request cause data input."),
        @ApiResponse(code = 404, message = "Not found."),
        @ApiResponse(code = 403, message = "Access Denied Or Any More."),
        @ApiResponse(code = 401, message = "Unauthorized.")})
    public ResponseEntity<?> login(
            @ApiParam(value = "JSon object để đăng nhập") @Valid @RequestBody UserLoginDto userLoginDto, HttpServletRequest request)
            throws UnauthorizedException {
        userLoginDto.setIp(request.getRemoteAddr());
        userLoginDto.setUserAgent(request.getHeader("User-Agent"));

        log.info(userLoginDto.getUname() + "|login");
        return ResponseEntity.ok(ResponseBody.builder().body(acountManagement.login(userLoginDto))
                .message(messageSourceVi.getMessageVi(OK002)).build());
    }

    @GetMapping("/logout")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(notes = "API sẽ được gọi trong trường hợp Logout ",
            value = "(Account Management Page) API Logout",
            authorizations = {
                @Authorization(value = StringUtils.API_KEY)})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Data Response Retrieved.", response = String.class),
        @ApiResponse(code = 500, message = "Internal Server Error."),
        @ApiResponse(code = 400, message = "Bad Request cause data input."),
        @ApiResponse(code = 404, message = "Not found."),
        @ApiResponse(code = 403, message = "Access Denied Or Any More."),
        @ApiResponse(code = 401, message = "Unauthorized.")})
    public ResponseEntity<?> logout() {

        userSessionService.delete(ApplicationUtils.getCurrentUser().getUserId());
        return ResponseEntity.ok(messageSourceVi.getMessageVi(OK002));
    }

    @PostMapping("/search")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(notes = "API sẽ được gọi trong trường hợp Search account ",
            value = "(Account Management Page) API Search account ",
            authorizations = {
                @Authorization(value = StringUtils.API_KEY)})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Data Response Retrieved.", response = UserBasicDto.class),
        @ApiResponse(code = 500, message = "Internal Server Error."),
        @ApiResponse(code = 400, message = "Bad Request cause data input."),
        @ApiResponse(code = 404, message = "Not found."),
        @ApiResponse(code = 403, message = "Access Denied Or Any More."),
        @ApiResponse(code = 401, message = "Unauthorized.")})
    public ResponseEntity<?> search(
            @ApiParam(value = "Số trang", defaultValue = "0") @RequestParam(name = "page",
                    defaultValue = "0") int page,
            @ApiParam(value = "Số bản ghi / trang", defaultValue = "10") @RequestParam(name = "size",
                    defaultValue = "10") int size,
            @ApiParam(value = "Sắp xếp theo các thuộc tính", defaultValue = "userId") @RequestParam(
                    name = "properties", defaultValue = "userId", required = true) String sortByProperties,
            @ApiParam(value = "Loại sắp xếp ", defaultValue = "ASC") @RequestParam(name = "sortBy",
                    defaultValue = "ASC") String sortBy,
            @ApiParam(value = "Từ khóa tìm kiếm: có thể tìm theo userName, fullName,email,phoneNumber.") @RequestParam(name = "keyword",
                    required = false) String keyword,
            @ApiParam(
                    value = "Tìm kiếm theo các tiêu chí ") @Valid @RequestBody UserSearchDto userSearchDto) throws PermissionException {
//        ApplicationUtils.checkHasRole(PermissionEnum.ADMIN.getRoleCode());
        return ResponseEntity.ok(ResponseBody.builder().message(messageSourceVi.getMessageVi(OK002))
                .body(acountManagement.searchAccount(keyword, page, size, sortByProperties, sortBy, userSearchDto))
                .build());
    }

    @GetMapping("/generateOtp/{email}")
    public ResponseEntity<?> generateOTP(@PathVariable("email") String email) throws MessagingException, BadRequestException {

        acountManagement.generateOTP(email);

        return ResponseEntity
                .ok(ResponseBody.builder().body("OTP has been sent to your email").message(messageSourceVi.getMessageVi("OK002")).build());
    }

    @RequestMapping(value ="/validateOtp", method = RequestMethod.POST)
    public ResponseEntity<?> validateOtp(@RequestBody ValidateOtpDTO validateOtpDTO) throws MessagingException, BadRequestException {

        return ResponseEntity.ok(ResponseBody.builder().body(acountManagement.validateOtp(validateOtpDTO)).message(messageSourceVi.getMessageVi("OK002")).build());
    }

    @RequestMapping(value ="/validateOtpAndChangePass", method = RequestMethod.POST)
    public ResponseEntity<?> validateOtpAndChangePass(@RequestBody ValidateOtpDTOAndChangePass validateOtpDTO) throws MessagingException, BadRequestException {

        return ResponseEntity.ok(ResponseBody.builder().body(acountManagement.validateOtpAndChangePass(validateOtpDTO)).message(messageSourceVi.getMessageVi("OK002")).build());
    }
}
