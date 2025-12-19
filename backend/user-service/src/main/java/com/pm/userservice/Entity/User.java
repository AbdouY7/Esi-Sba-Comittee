package com.pm.userservice.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String username;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String phone;

    @NotNull
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private     CommitteePosition committeePosition;

    private Double salary;


    private LocalDateTime CreatedAt;
    private LocalDateTime UpdatedAt;

    // to get the created dateTime & updated dateTime
    @PrePersist
    protected  void OnCreate() {
        CreatedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected  void OnUpdate() {
        UpdatedAt = LocalDateTime.now();
    }
}
