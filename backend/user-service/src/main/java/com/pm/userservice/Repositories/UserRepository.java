package com.pm.userservice.Repositories;

import com.fasterxml.jackson.databind.introspect.AnnotationCollector;
import com.pm.userservice.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getUserById(Long id);
    boolean existsByEmail(String email);
}
