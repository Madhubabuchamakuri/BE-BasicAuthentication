package com.BasicLogin.service.impl;

import com.BasicLogin.dto.LoginRequest;
import com.BasicLogin.dto.LoginResponse;
import com.BasicLogin.dto.OTPValidateRequest;
import com.BasicLogin.dto.commonResponse.ApiResponse;
import com.BasicLogin.dto.commonResponse.SuccessLoginResponse;
import com.BasicLogin.entity.AppUser;
import com.BasicLogin.entity.OTP;
import com.BasicLogin.entity.UserToken;
import com.BasicLogin.repo.AppUserRepo;
import com.BasicLogin.repo.OTPRepo;
import com.BasicLogin.repo.UserTokenRepo;
import com.BasicLogin.service.AuthenticationService;
import com.BasicLogin.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private AppUserRepo appUserRepo;
    @Autowired
    private UserTokenRepo userTokenRepo;
    @Autowired
    private OTPRepo otpRepo;
    @Value("${email.from}")
    private String from;

    @Override
    public ResponseEntity<ApiResponse> login(LoginRequest loginRequest) {
        ResponseEntity<ApiResponse> response = null;
        response = validateLoginRequest(loginRequest);
        if (response != null) {
            return response;
        }
        Optional<AppUser> user = appUserRepo.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        if (user.isPresent()) {
            AppUser appUser = user.get();
            List<UserToken> userToken = userTokenRepo.findByUserAndIsActiveTrue(appUser);
            if (!userToken.isEmpty()) {
                ApiResponse apiResponse = new ApiResponse(HttpStatus.FORBIDDEN, "a user is logged in with credentials");
                return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
            }
            String otp = CommonUtil.genearteOTP(4);
            String message = otp + " is your otp for login ";
            try {
                CommonUtil.sendEmail(appUser.getEmail(), message, "OTP:Login", from);
            } catch (UnknownHostException | MessagingException e) {
                ApiResponse apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),e);
                return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
            }
            OTP userOtp = new OTP();
            userOtp.setOtp(otp);
            userOtp.setUser(appUser);
            userOtp.setIsActive(Boolean.TRUE);
            otpRepo.save(userOtp);
            ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "OTP has been sent to your email");
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        } else {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "email and password is incorrect");
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }
    }

    @Override
    public ResponseEntity<ApiResponse> otpValidation(OTPValidateRequest validateRequest) {
        ResponseEntity<ApiResponse> response = null;
        response = validateOTPRequest(validateRequest);
        if (response != null) {
            return response;
        }
        Optional<AppUser> optionalUser = appUserRepo.findByEmail(validateRequest.getEmail());
        AppUser appUser = null;
        appUser = optionalUser.orElseGet(AppUser::new);

        Optional<OTP> otp = otpRepo.findByUserAndOtpAndIsActiveTrue(appUser, validateRequest.getOtp());
        if (otp.isPresent()) {
            String token = CommonUtil.generateRandomToken();
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setFullName(appUser.getFullName());
            loginResponse.setToken(token);
            OTP updateOTP = otp.get();
            updateOTP.setIsActive(Boolean.FALSE);
            UserToken userToken = new UserToken();
            userToken.setIsActive(Boolean.TRUE);
            userToken.setJwt(token);
            userToken.setUser(appUser);
            userTokenRepo.save(userToken);
            otpRepo.save(updateOTP);
            ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "otp verified", loginResponse);
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        } else {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "in-valid otp");
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }
    }

    private ResponseEntity<ApiResponse> validateLoginRequest(LoginRequest loginRequest) {
        if (!StringUtils.hasLength(loginRequest.getEmail()) || !StringUtils.hasLength(loginRequest.getPassword())) {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "email and password is required");
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }
        return null;
    }

    private ResponseEntity<ApiResponse> validateOTPRequest(OTPValidateRequest loginRequest) {
        if (!StringUtils.hasLength(loginRequest.getEmail()) || !StringUtils.hasLength(loginRequest.getOtp())) {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "OTP and email is required");
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> logOutUser(String email) {
        ResponseEntity<ApiResponse> response = null;
        Optional<AppUser> user = appUserRepo.findByEmail(email);
        if (user.isPresent()) {
            AppUser appUser = user.get();
            List<UserToken> userToken = userTokenRepo.findByUserAndIsActiveTrue(appUser);
            if (!userToken.isEmpty()) {
                userToken.forEach(token -> token.setIsActive(Boolean.FALSE));
                userTokenRepo.saveAll(userToken);
            }
            ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "User Logged out successfully");
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());

        } else {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "No User Found");
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }
    }

    @Override
    public ResponseEntity<ApiResponse> logOut(String loginToken) {
        List<UserToken> userToken = userTokenRepo.findByJwtAndIsActiveTrue(loginToken);
        if (!userToken.isEmpty()) {
            userToken.forEach(token -> token.setIsActive(Boolean.FALSE));
            userTokenRepo.saveAll(userToken);
            ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "User Logged out successfully");
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        } else {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "User has already been logged out");
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }
    }

    @Override
    public ResponseEntity<ApiResponse> successLogin(String token) {
        List<UserToken> userToken = userTokenRepo.findByJwtAndIsActiveTrue(token);
        if (userToken.isEmpty()) {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.UNAUTHORIZED, "Login to access the url");
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }
        SuccessLoginResponse loginResponse = new SuccessLoginResponse();
        loginResponse.setMessage("thanks for logging in");
        ApiResponse response = new ApiResponse(HttpStatus.OK, "thanks for logging in", loginResponse);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
