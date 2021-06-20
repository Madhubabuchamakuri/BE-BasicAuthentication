package com.BasicLogin.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Document
public class OTP extends BaseEntity {
    private String otp;
    @DBRef
    @Indexed
    private AppUser user;
    private Boolean isActive;
}
