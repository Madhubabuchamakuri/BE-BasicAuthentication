package com.BasicLogin.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Document
public class AppUser extends BaseEntity {
    private String firstName;
    private String lastName;
    private String email;
    private Boolean isActive = Boolean.TRUE;
    private String password;
    @DBRef
    private List<OTP> userOtp = new ArrayList<>();

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
