package com.BasicLogin.service;

import com.BasicLogin.dto.SignUpDto;
import com.BasicLogin.dto.commonResponse.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface UserSignUpService {
    ResponseEntity<ApiResponse> registerUser(SignUpDto signUpDto);
}
