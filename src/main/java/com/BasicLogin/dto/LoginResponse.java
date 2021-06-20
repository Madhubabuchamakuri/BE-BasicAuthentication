package com.BasicLogin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

    private String fullName;
    private String token;
}
