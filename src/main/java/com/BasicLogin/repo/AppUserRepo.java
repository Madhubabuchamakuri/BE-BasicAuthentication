package com.BasicLogin.repo;

import com.BasicLogin.entity.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepo extends MongoRepository<AppUser, String> {
    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByEmailAndPassword(String email, String password);
}
