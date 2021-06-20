package com.BasicLogin.service.impl;

import com.BasicLogin.dto.SignUpDto;
import com.BasicLogin.dto.commonResponse.ApiResponse;
import com.BasicLogin.dto.commonResponse.SuccessLoginResponse;
import com.BasicLogin.entity.AppUser;
import com.BasicLogin.repo.AppUserRepo;
import com.BasicLogin.service.UserSignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserSignUpServiceImpl implements UserSignUpService {
    @Autowired
    private AppUserRepo appUserRepo;

    @Override
    public ResponseEntity<ApiResponse> registerUser(SignUpDto signUpDto) {
        ResponseEntity<ApiResponse> response = null;
        response = validateUserDetails(signUpDto);
        if (response != null) {
            return response;
        }
        if (!signUpDto.getPassword().equals(signUpDto.getConfirmPassword())) {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "password and confirm password does not match");
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }
        Optional<AppUser> existingUser = appUserRepo.findByEmail(signUpDto.getEmail());
        if (existingUser.isPresent()) {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "user exists with given email");
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }
        AppUser appUser = new AppUser();
        appUser.setFirstName(signUpDto.getFirstName());
        appUser.setLastName(signUpDto.getLastName());
        appUser.setIsActive(Boolean.TRUE);
        appUser.setPassword(signUpDto.getPassword());
        appUser.setEmail(signUpDto.getEmail());
        appUser = appUserRepo.save(appUser);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "User registered Successfully");
        response = new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        return response;
    }

    private ResponseEntity<ApiResponse> validateUserDetails(SignUpDto signUpDto) {
        if (!StringUtils.hasLength(signUpDto.getEmail()) || !StringUtils.hasLength(signUpDto.getFirstName()) || !StringUtils.hasLength(signUpDto.getLastName()) || !StringUtils.hasLength(signUpDto.getPassword()) || !StringUtils.hasLength(signUpDto.getConfirmPassword())) {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "fill all mandatory fields");
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }
        return null;
    }

}
