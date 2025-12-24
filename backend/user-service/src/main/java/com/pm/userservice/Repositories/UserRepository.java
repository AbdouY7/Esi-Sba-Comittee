package com.pm.userservice.Repositories;

import com.pm.userservice.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getUserById(Long id);
    boolean existsByEmail(String email);

    Optional<User> findUserByEmail(String email);

    User findUserByVerificationToken(String verificationToken);
}
