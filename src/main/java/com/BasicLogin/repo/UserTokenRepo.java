package com.BasicLogin.repo;

import com.BasicLogin.entity.AppUser;
import com.BasicLogin.entity.UserToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTokenRepo extends MongoRepository<UserToken, String> {
    List<UserToken> findByUserAndIsActiveTrue(AppUser email);

    List<UserToken> findByJwtAndIsActiveTrue(String token);
}
