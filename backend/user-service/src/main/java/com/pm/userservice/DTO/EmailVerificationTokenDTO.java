package com.pm.userservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerificationTokenDTO {
    private String token;
    private LocalDateTime expiryDate;
    private String email;
}
