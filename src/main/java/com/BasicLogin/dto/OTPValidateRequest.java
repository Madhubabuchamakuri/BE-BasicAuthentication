package com.BasicLogin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OTPValidateRequest {
    private String email;
    private String otp;
}
