package com.BasicLogin.controller;

import com.BasicLogin.dto.SignUpDto;
import com.BasicLogin.dto.commonResponse.ApiResponse;
import com.BasicLogin.service.UserSignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserSignUpService userSignUpService;

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse> singUp(@RequestBody SignUpDto signUpDto) {
        return userSignUpService.registerUser(signUpDto);
    }
}
