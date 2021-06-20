package com.BasicLogin.service;

import com.BasicLogin.dto.LoginRequest;
import com.BasicLogin.dto.OTPValidateRequest;
import com.BasicLogin.dto.commonResponse.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    ResponseEntity<ApiResponse> login(LoginRequest loginRequest);

    ResponseEntity<ApiResponse> otpValidation(OTPValidateRequest validateRequest);

    ResponseEntity<ApiResponse> logOutUser(String email);

    ResponseEntity<ApiResponse> logOut(String loginToken);

    ResponseEntity<ApiResponse> successLogin(String token);
}
