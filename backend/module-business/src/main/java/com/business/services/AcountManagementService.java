package com.business.services;

import com.business.authencation.CustomUserDetails;
import com.business.authencation.JwtAuthenticationResponse;
import com.core.model.account.*;
import com.core.entity.User;
import com.core.exception.BadRequestException;
import com.core.exception.UnauthorizedException;
import com.core.model.user.UserDTO;
import org.springframework.data.domain.Page;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author sadfsafbhsaid
 */
public interface AcountManagementService {

    List<User> checkAlreadyUsernameExist(String username);

    List<User> checkAlreadyEmailExist(String email);

    List<User> checkAlreadyPhoneExist(String phoneNumber);

    List<User> getAllllll();
    UserBasicDto createNewAccount(UserRegisterDto userRegisterDto) throws BadRequestException;

    UserBasicDto updateAccount(UserUpdateDto userUpdateDto)
            throws BadRequestException;
    UserBasicDto updateAccountByUser(UserUpdateDtoByUser userUpdateDto)  throws BadRequestException;

    User deleteAccount(Long id);

    Page<UserBasicDto> searchAccount(String keyword,int pageNumber, int size, String sortByProperties, String sortBy,
                                     UserSearchDto userSearchDto);

    UserBasicDto getUserById(Long userId);

    User findUserById(Long userId);

    User findByUserIdAndIsDelete(Long userId, Long isDelete);

    User findUserByUsername(String username);

    User save(User user);

    JwtAuthenticationResponse refreshToken(CustomUserDetails customUserDetails);

    JwtAuthenticationResponse login(UserLoginDto userLoginDto) throws UnauthorizedException;

    Set<User> findUserByIds(List<Long> userIds);

    UserDTO changePass(UserChangePassAdminDto userChangePassDto) throws BadRequestException;

    UserDTO changePass_(UserChangePassDto userChangePassDto) throws BadRequestException;

    boolean checkExitsByEmail(String email);

    void generateOTP(String email) throws MessagingException, BadRequestException;

    String validateOtp(ValidateOtpDTO otpnum) throws BadRequestException;

    String validateOtpAndChangePass(ValidateOtpDTOAndChangePass validateOtpDTO) throws BadRequestException;
}

