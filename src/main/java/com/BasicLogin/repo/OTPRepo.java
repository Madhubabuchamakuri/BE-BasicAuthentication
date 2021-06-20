package com.BasicLogin.repo;

import com.BasicLogin.entity.AppUser;
import com.BasicLogin.entity.OTP;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPRepo extends MongoRepository<OTP, String> {
    Optional<OTP> findByUserAndOtpAndIsActiveTrue(AppUser email, String otp);
}
