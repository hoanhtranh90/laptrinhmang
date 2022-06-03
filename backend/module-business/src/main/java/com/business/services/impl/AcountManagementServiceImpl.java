package com.business.services.impl;

import com.business.services.*;
import com.business.authencation.CustomUserDetails;
import com.business.authencation.JwtAuthenticationResponse;
import com.business.authencation.JwtTokenProvider;
import com.business.utilts.ApplicationUtils;
import com.core.entity.*;
import com.core.mapper.MapperObject;
import com.core.model.account.*;
import com.business.services.AcountManagementService;
import com.core.config.ApplicationConfig.MessageSourceVi;
import com.core.constants.ActionLogEnum;
import com.core.constants.PermissionEnum;
import com.core.constants.StatusEnum;
import com.core.constants.TypeLogEnum;
import com.core.exception.BadRequestException;
import com.core.exception.PermissionException;
import com.core.exception.UnauthorizedException;
import com.core.model.user.UserDTO;
import com.core.repository.*;
import com.core.utils.Constants;
import com.core.utils.DateUtils;
import com.core.utils.H;
import com.core.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.business.utilts.ApplicationUtils.isLogin;

/**
 *
 * @author sangnk
 */
@Service
@Primary
@Slf4j
@Qualifier("AcountManagementService_Main")
@Transactional
public class AcountManagementServiceImpl implements AcountManagementService {

    @Value(value = "${account.management}")
    private String accountManager;

    @Value(value = "${password.management}")
    private String passwordManager;

    @Value(value = "${email.management}")
    private String emailManager;

    @Value(value = "${firstname.management}")
    private String firstNameManager;

    @Value(value = "${lastname.management}")
    private String lastNameManager;

    @Value(value = "${organization.management}")
    private String organizationManager;

    @Value(value = "${phonenumber.management}")
    private String phoneNumberManager;

    @Autowired
    private MessageSourceVi messageSourceVi;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ActionLogService actionLogService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OTPService otpService;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;


    private final long dateNow = DateUtils.getDateAllEndDate(new Date()).getTime();

    @Bean
    public CommandLineRunner initialAccountAdminAndApplication() {

        return (args) -> {
            if (!H.isTrue(userRepository.findByUserName(accountManager, 0L))) {
                Role role = roleRepository.findByRoleCode(PermissionEnum.ADMIN.getRoleCode());
                User user = User.builder().email(emailManager).fullName(firstNameManager)
                        .phoneNumber(phoneNumberManager)
                        .isDelete(Constants.DELETE.NORMAL)
                        .password(passwordEncoder.encode(passwordManager))
                        .status(StatusEnum.ACTIVE.getStatus())
                        .status(StatusEnum.ACTIVE.getStatus()).userName(accountManager)
                        .userId(Long.valueOf(1)).build();

                UserRole userRole = UserRole.builder().role(role).user(userRepository.save(user))
                        .userRoleId(Long.valueOf(1)).build();

                userRoleRepository.save(userRole);
                log.info("Created account management.");
            } else {
                log.info("Account management already exsit.");
            }

        };
    }

    /**
     * @param session
     * @param userLogin
     */
    private synchronized UserSession getUserSession(String session, User userLogin) {
        UserSession userSession = userSessionRepository.findAllByUserUserId(userLogin.getUserId());
        if (null != userSession) {
            userSession.setSession(session);
        } else {
            userSession = UserSession.builder().session(session).user(userLogin).build();
        }
        return userSession;

    }

    @Override
    public synchronized UserBasicDto createNewAccount(UserRegisterDto userBasicDto) throws BadRequestException {
        try {
            // Check Username
            List<User> userCheckUsernames
                    = checkAlreadyUsernameExist(userBasicDto.getUserName());
            if (!userCheckUsernames.isEmpty()) {
                throw new BadRequestException(messageSourceVi.getMessageVi("ER002", new Object[]{userBasicDto.getUserName()}));
            }

//            if (!ObjectUtils.isEmpty(userBasicDto.getEmail())) {
//                // Check email
//                List<User> userCheckEmails
//                        = checkAlreadyEmailExist(userBasicDto.getEmail());
//
//                if (!userCheckEmails.isEmpty()) {
//                    throw new BadRequestException(messageSourceVi.getMessageVi("ER002", new Object[]{userBasicDto.getEmail()}));
//                }
//            }
//            // Check Phone
//            if (!ObjectUtils.isEmpty(userBasicDto.getPhoneNumber())) {
//                List<User> userCheckPhoness
//                        = checkAlreadyPhoneExist(userBasicDto.getPhoneNumber());
//                if (!userCheckPhoness.isEmpty()) {
//                    throw new BadRequestException(messageSourceVi.getMessageVi("ER002", new Object[]{userBasicDto.getPhoneNumber()}));
//                }
//            }
            User userRegister = checkAndUpdateBranches(userBasicDto);

            userRegister.setIsDelete(Constants.DELETE.NORMAL);
            userRegister.setStatus(StatusEnum.ACTIVE.getStatus());

            Role role = roleRepository.findByRoleCode(Constants.ROLE.USER);
            UserRole userRole = UserRole.builder()
                    .role(role)
                    .user(userRepository.save(userRegister))
                    .isDelete(Constants.DELETE.NORMAL)
                    .status(StatusEnum.ACTIVE.getStatus())
                    .build();
            userRoleRepository.save(userRole);

            return getUserByUser(userRegister);
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }


    }

    /**
     * @param userBasicDto
     * @return
     * @throws PermissionException
     */
    private User checkAndUpdateBranches(UserRegisterDto userBasicDto) {
        User userRegister = MapperObject.userBuilder(userBasicDto);
        userRegister.setPassword(passwordEncoder.encode(userBasicDto.getPassword()));
        userRegister.setStatus(userBasicDto.getStatus() != null ? userBasicDto.getStatus() : StatusEnum.ACTIVE.getStatus());
//        userRegister.setCreateByUserId(ApplicationUtils.getCurrentUser().getUserId());
        userRegister.setCreatedBy("System");
        return userRegister;
    }

    @Override
    public User deleteAccount(Long id) {
        User user = userRepository.findByIdCustom(id, Constants.DELETE.NORMAL);
        if (user != null) {
            try {
                user.setIsDelete(Constants.DELETE.NORMAL);
                User user1 = userRepository.save(user);
                return user1;
            } catch (Exception ex) {
                Logger.getLogger(AcountManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return null;
        //userRepository.deleteByUserIdAndUsername(userBasicDto.getUserId(), userBasicDto.getUsername());
    }

    @Override
    public Page<UserBasicDto> searchAccount(String keyword, int pageNumber, int size, String sortByProperties, String sortBy,
                                            UserSearchDto userSearchDto) {
        Sort sort = ApplicationUtils.getSort(sortByProperties, sortBy);
        Pageable pageable = PageRequest.of(pageNumber, size, sort);
        keyword = StringUtils.buildLikeExp(keyword);
        if (checkUserSearch(userSearchDto)) {
            keyword = null;
        }
        List<UserBasicDto> userBasicDtos = new ArrayList<>();
        Page<User> page = userRepository.searchAllUser(
                Constants.DELETE.NORMAL,
                keyword,
                StringUtils.buildLikeExp(userSearchDto.getUserName()),
                StringUtils.buildLikeExp(userSearchDto.getFullName()),
                StringUtils.buildLikeExp(userSearchDto.getPhoneNumber()),
                StringUtils.buildLikeExp(userSearchDto.getEmail()),
                userSearchDto.getRoleCode(),
                pageable);
        page.getContent().stream().forEach(u -> {
            userBasicDtos.add(getUserByUser(u));
        });
        return new PageImpl<>(userBasicDtos, pageable, page.getTotalElements());

    }

    @Override
    public List<User> getAllllll() {
        return userRepository.findAll();
    }

    public boolean checkUserSearch(UserSearchDto userSearchDto) {
        return StringUtils.isTrue(userSearchDto.getEmail()) || StringUtils.isTrue(userSearchDto.getDeptId())
                || StringUtils.isTrue(userSearchDto.getFullName()) || StringUtils.isTrue(userSearchDto.getPhoneNumber())
                || StringUtils.isTrue(userSearchDto.getUserName());
    }

    @Override
    public synchronized UserBasicDto updateAccount(UserUpdateDto userUpdateDto)
            throws BadRequestException {
        try {
            User userOld = findUserById(userUpdateDto.getUserId());

            if (null == userOld) {
                throw new BadRequestException(messageSourceVi.getMessageVi("USERNAME_NOT_FOUND_MSG"));
            }
            if (!ObjectUtils.isEmpty(userUpdateDto.getUserName()) && (null != userOld.getUserName()
                    && !userOld.getUserName().equalsIgnoreCase(userUpdateDto.getUserName()))) {
                // Check Username
                List<User> userCheckUsernames
                        = checkAlreadyUsernameExist(userUpdateDto.getUserName());
                if (!userCheckUsernames.isEmpty()) {
                    throw new BadRequestException(messageSourceVi.getMessageVi("ER002", new Object[]{userUpdateDto.getUserName()}));
                }
            }
            if(StringUtils.isTrue(userUpdateDto.getFullName())){
                userOld.setFullName(userUpdateDto.getFullName());
            }
            if(StringUtils.isTrue(userUpdateDto.getUserName())){
                userOld.setUserName(userUpdateDto.getUserName());
            }
            if(StringUtils.isTrue(userUpdateDto.getEmail())){
                userOld.setEmail(userUpdateDto.getEmail());
            }
            if(StringUtils.isTrue(userUpdateDto.getPhoneNumber())){
                userOld.setPhoneNumber(userUpdateDto.getPhoneNumber());
            }
            if(StringUtils.isTrue(userUpdateDto.getSex())){
                userOld.setSex(userUpdateDto.getSex());
            }
            if(StringUtils.isTrue(userUpdateDto.getAddress())){
                userOld.setAddress(userUpdateDto.getAddress());
            }
            if(StringUtils.isTrue(userUpdateDto.getDateOfBirth())){
                userOld.setDateOfBirth(userUpdateDto.getDateOfBirth());
            }
            if(StringUtils.isTrue(userUpdateDto.getStatus())){
                userOld.setStatus(userUpdateDto.getStatus() != null ? userUpdateDto.getStatus() : userOld.getStatus());

            }
            userRepository.save(userOld);
            return getUserByUser(userOld);

        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public UserBasicDto updateAccountByUser(UserUpdateDtoByUser userUpdateDto) throws BadRequestException {
        try {
            User userOld = ApplicationUtils.getCurrentUser();

            if (null == userOld) {
                throw new BadRequestException(messageSourceVi.getMessageVi("USERNAME_NOT_FOUND_MSG"));
            }
            if (!ObjectUtils.isEmpty(userUpdateDto.getUserName()) && (null != userOld.getUserName()
                    && !userOld.getUserName().equalsIgnoreCase(userUpdateDto.getUserName()))) {
                // Check Username
                List<User> userCheckUsernames
                        = checkAlreadyUsernameExist(userUpdateDto.getUserName());
                if (!userCheckUsernames.isEmpty()) {
                    throw new BadRequestException(messageSourceVi.getMessageVi("ER002", new Object[]{userUpdateDto.getUserName()}));
                }
            }
            if(StringUtils.isTrue(userUpdateDto.getFullName())){
                userOld.setFullName(userUpdateDto.getFullName());
            }
            if(StringUtils.isTrue(userUpdateDto.getUserName())){
                userOld.setUserName(userUpdateDto.getUserName());
            }
            if(StringUtils.isTrue(userUpdateDto.getEmail())){
                userOld.setEmail(userUpdateDto.getEmail());
            }
            if(StringUtils.isTrue(userUpdateDto.getPhoneNumber())){
                userOld.setPhoneNumber(userUpdateDto.getPhoneNumber());
            }
            if(StringUtils.isTrue(userUpdateDto.getSex())) {
                userOld.setSex(userUpdateDto.getSex());
            }
            if(StringUtils.isTrue(userUpdateDto.getAddress())){
                userOld.setAddress(userUpdateDto.getAddress());
            }
            if(StringUtils.isTrue(userUpdateDto.getDateOfBirth())){
                userOld.setDateOfBirth(userUpdateDto.getDateOfBirth());
            }
            if(StringUtils.isTrue(userUpdateDto.getStatus())){
                userOld.setStatus(userUpdateDto.getStatus() != null ? userUpdateDto.getStatus() : userOld.getStatus());

            }
            userRepository.save(userOld);
            return getUserByUser(userOld);

        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findByIdCustom(userId, Constants.DELETE.NORMAL);

    }

    @Override
    public User findByUserIdAndIsDelete(Long userId, Long isDelete) {
        return userRepository.findByUserIdAndIsDelete(userId, isDelete);
    }

    @Override
    public UserBasicDto getUserById(Long userId) {
        User userForUpdate = userRepository.findByIdCustom(userId, Constants.DELETE.NORMAL);
        return getUserByUser(userForUpdate);
    }

    public UserBasicDto getUserByUser(User userForUpdate) {

        if (null != userForUpdate) {
            UserBasicDto basicDto = modelMapper.map(userForUpdate, UserBasicDto.class);

            if(isLogin()) basicDto.setIsFollow(isFollow(userForUpdate));

            if (StringUtils.isTrue(userForUpdate.getUserRoleList())) {
                List<String> roles = new ArrayList<>();
                userForUpdate.getUserRoleList().stream().forEach(u -> {
                    roles.add(u.getRole().getRoleCode());
                });
                basicDto.setRoleCodes(roles);
            }
            basicDto.setStatusStr(StatusEnum.getStatusDescription(basicDto.getStatus()));
            return basicDto;
        }
        return null;
    }
    private Long isFollow(User user) {
        User currentUser = ApplicationUtils.getCurrentUser();
        Follow follow = followRepository.findByFollowerAndFollowing(currentUser.getUserId(), user.getUserId());
        if (follow == null) {
            return 0L;
        }
        return 1L;
    }
    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUserName(username, Constants.DELETE.NORMAL);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> checkAlreadyEmailExist(String email) {
        return userRepository.checkAlreadyEmailExsit(email, Constants.DELETE.NORMAL);
    }

    @Override
    public List<User> checkAlreadyPhoneExist(String phoneNumber) {
        return userRepository.checkAlreadyPhoneExsit(phoneNumber, Constants.DELETE.NORMAL);
    }

    @Override
    public List<User> checkAlreadyUsernameExist(String username) {
        return userRepository.checkAlreadyUsernameExsit(username, Constants.DELETE.NORMAL);
    }

    @Override
    public JwtAuthenticationResponse refreshToken(CustomUserDetails customUserDetails) {
        // Kiểm tra nếu user tồn tại
        customUserDetails.setUser(userRepository.findByIdCustom(customUserDetails.getUser().getUserId(), Constants.DELETE.NORMAL));
        // Kiểm tra nếu session tồn tại
        customUserDetails.setSession(userSessionRepository.findAllByUserUserId(customUserDetails.getUser().getUserId()).getSession());

        try {
            return JwtAuthenticationResponse.builder().accessToken(tokenProvider.generateToken(customUserDetails)).build();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AcountManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AcountManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(AcountManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = UnauthorizedException.class)
    public JwtAuthenticationResponse login(UserLoginDto userLoginDto) throws UnauthorizedException {
        Authentication authentication = null;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userLoginDto.getUname(), userLoginDto.getPwd()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            String session = servletRequestAttributes.getSessionId();

            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            User userLogin = customUserDetails.getUser();
            customUserDetails.setSession(session);

            UserSession userSession = getUserSession(session, userLogin);
            userSessionRepository.save(userSession);
            JwtAuthenticationResponse authenticationResponse = JwtAuthenticationResponse.builder().accessToken(tokenProvider.generateToken(customUserDetails)).build();
//            actionLogService.addLog(null, ActionLogEnum.DANG_NHAP.getAction(), TypeLogEnum.NOIBO.getType(), userLogin.getUserId());
            return authenticationResponse;
        } catch (BadCredentialsException e) {
            log.error(userLoginDto.getUname() + "|" + e.getMessage());
            throw new UnauthorizedException(messageSourceVi.getMessageVi("MSG_LOGIN_FAILED"));
        } catch (AccessDeniedException ex1) {
            log.error(userLoginDto.getUname() + "|" + ex1.getMessage());
            throw new AccessDeniedException(ex1.getMessage());
        } catch (LockedException ex) {
            log.error(userLoginDto.getUname() + "|" + ex.getMessage());
            throw new UnauthorizedException(messageSourceVi.getMessageVi("MSG_LOCKED_ACCOUNT"));
        } catch (DisabledException exx) {
            log.error(userLoginDto.getUname() + "|" + exx.getMessage());
            throw new UnauthorizedException(messageSourceVi.getMessageVi("MSG_DISABLE_ACCOUNT"));
        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException ex) {
            throw new UnauthorizedException(ex.getMessage());
        }
    }

    @Override
    public Set<User> findUserByIds(List<Long> userIds) {
        List<User> list = userRepository.findAllById(userIds);
        Set<User> set = new HashSet<>(list);
        return set;
    }

    @Override
    public UserDTO changePass(UserChangePassAdminDto userChangePassDto) throws BadRequestException {

        User user = userRepository.findByIdCustom(userChangePassDto.getUserId(), Constants.DELETE.NORMAL);
        if (user == null) {
            throw new BadRequestException(messageSourceVi.getMessageVi("MSG_USER_NOT_EXIST"));
        }
        if(!userChangePassDto.getNewPass().equals(userChangePassDto.getConfirmPass())){
            throw new BadRequestException("Nhập lại mật khẩu không đúng");
        }
        user.setPassword(passwordEncoder.encode(userChangePassDto.getNewPass()));
        userRepository.save(user);
        return modelMapper.map(user, UserDTO.class);


    }

    @Override
    public UserDTO changePass_(UserChangePassDto userChangePassDto) throws BadRequestException {
        User user = ApplicationUtils.getCurrentUser();
        if (user == null) {
            throw new BadRequestException(messageSourceVi.getMessageVi("MSG_USER_NOT_EXIST"));
        }
        if(!userChangePassDto.getNewPass().equals(userChangePassDto.getConfirmPass())){
            throw new BadRequestException("Nhập lại mật khẩu không đúng");
        }
        //compare old pass decode
        if (passwordEncoder.matches(userChangePassDto.getOldPass(), user.getPassword())) {
            if(checkContains(userChangePassDto.getNewPass(), userChangePassDto.getOldPass())){
                throw new BadRequestException("Mật khẩu mới có chứa nhiều hơn 5 ký tự với mật khẩu cũ");
            }
            user.setPassword(passwordEncoder.encode(userChangePassDto.getNewPass()));
            userRepository.save(user);
            return modelMapper.map(user, UserDTO.class);
        } else {
            throw new BadRequestException(messageSourceVi.getMessageVi("MSG_OLD_PASS_NOT_MATCH"));
        }
    }

    @Override
    public boolean checkExitsByEmail(String email) {
        User user = userRepository.existsByEmail(email, Constants.DELETE.NORMAL);
        return H.isTrue(user);
    }

    @Override
    public void generateOTP(String email) throws MessagingException, BadRequestException {
        User user = userRepository.findByEmail(email, Constants.DELETE.NORMAL);
        if (!H.isTrue(user)) {
            throw new BadRequestException(messageSourceVi.getMessageVi("ER024", new Object[]{"Email"}));
        }
        String getEmail = user.getEmail();
        Long otp = otpService.generateOTP(getEmail);
        emailService.sendEmail("Khôi phục tài khoản", emailService.buildEmailSendOtpForResetPassWord(user, otp), Collections.singletonList(getEmail));
//        EmailTemplate template = new EmailTemplate("SendOtp.html");
//        Map replacements = new HashMap();
//        replacements.put("user", username);
//        replacements.put("otpnum", String.valueOf(otp));
//        String message = template.getTemplate(replacements);
//
//        emailService.sendOtpMessage("nayanajain854@gmail.com", "OTP -SpringBoot", message);
    }

    @Override
    public String validateOtp(ValidateOtpDTO validateOtpDTO) throws BadRequestException {
        final String FAIL = "OTP không đúng";
        //Validate the Otp
        if (validateOtpDTO.getOtp() >= 0) {
            Long serverOtp = otpService.getOtp(validateOtpDTO.getEmail());
            User user = userRepository.findByEmail(validateOtpDTO.getEmail(), Constants.DELETE.NORMAL);
            if (!H.isTrue(user)) {
                throw new BadRequestException(messageSourceVi.getMessageVi("ER024", new Object[]{"Email"}));
            }
            if (serverOtp > 0) {
                if (validateOtpDTO.getOtp() == serverOtp) {
                    return ("OK");
                } else {
                    throw new BadRequestException(FAIL);
                }
            } else {
                throw new BadRequestException(FAIL);
            }
        } else {
            throw new BadRequestException(FAIL);
        }
    }

    @Override
    public String validateOtpAndChangePass(ValidateOtpDTOAndChangePass validateOtpDTO) throws BadRequestException {
        final String FAIL = "OTP không đúng";
        //Validate the Otp
        if (validateOtpDTO.getOtp() >= 0) {
            Long serverOtp = otpService.getOtp(validateOtpDTO.getEmail());
            User user = userRepository.findByEmail(validateOtpDTO.getEmail(), Constants.DELETE.NORMAL);
            if (!H.isTrue(user)) {
                throw new BadRequestException(messageSourceVi.getMessageVi("ER024", new Object[]{"Email"}));
            }
            if (serverOtp > 0) {
                if (validateOtpDTO.getOtp() == serverOtp) {
                    if (!validateOtpDTO.getNewPassword().equals(validateOtpDTO.getConfirmPassword())) {
                        throw new BadRequestException("Nhập lại mật khẩu không đúng");
                    }
                    user.setPassword(passwordEncoder.encode(validateOtpDTO.getNewPassword()));
                    userRepository.save(user);
                    otpService.clearOTP(validateOtpDTO.getEmail());
                    return ("OK");
                } else {
                    throw new BadRequestException(FAIL);
                }
            } else {
                throw new BadRequestException(FAIL);
            }
        } else {
            throw new BadRequestException(FAIL);
        }
    }

    //Check if there is any common character in two given strings

    boolean checkContains(String s1, String s2) {
        int length = 5;

        for (int i = 0; i <= s1.length() - length; i++) {
            String sub1 = s1.substring(i, i + length);
            if (s2.contains(sub1)) {
                return true;
            }
        }

        return false;
    }


}
