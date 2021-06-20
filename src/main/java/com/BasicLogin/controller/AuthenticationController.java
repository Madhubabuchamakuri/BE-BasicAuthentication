package com.BasicLogin.controller;

import com.BasicLogin.dto.LoginRequest;
import com.BasicLogin.dto.OTPValidateRequest;
import com.BasicLogin.dto.SignUpDto;
import com.BasicLogin.dto.commonResponse.ApiResponse;
import com.BasicLogin.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> singUp(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @PostMapping("/opt-validation")
    public ResponseEntity<ApiResponse> validateOtp(@RequestBody OTPValidateRequest validateRequest) {
        return authenticationService.otpValidation(validateRequest);
    }

    @GetMapping("/force-logout/{email}")
    public ResponseEntity<ApiResponse> forceLogout(@PathVariable String email) {
        return authenticationService.logOutUser(email);
    }

    @GetMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader(value="Authorization") String token) {
        return authenticationService.logOut(token);
    }

    @GetMapping("/success")
    public ResponseEntity<ApiResponse> successLogin(@RequestHeader(value="Authorization") String token) {
        return authenticationService.successLogin(token);
    }
}
