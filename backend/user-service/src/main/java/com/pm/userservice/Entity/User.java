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
@Table(name = "users")
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


    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private     CommitteePosition committeePosition;

    private Double salary;

    private boolean isActive;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // to get the created dateTime & updated dateTime
    @PrePersist
    protected  void onCreate() {
        createdAt = LocalDateTime.now();
        isActive = false;
        if (role != Role.COMMITTEE) {
            committeePosition = null;
        }
    }
    @PreUpdate
    protected  void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (role != Role.COMMITTEE) {
            committeePosition = null;
        }
    }

    public boolean isValid() {
        // If role is COMMITTEE, committeePosition must not be null
        if (role == Role.COMMITTEE && committeePosition == null) {
            return false;
        }
        // If role is not COMMITTEE, committeePosition must be null
        if (role != Role.COMMITTEE && committeePosition != null) {
            return false;
        }
        return true;
    }
}
