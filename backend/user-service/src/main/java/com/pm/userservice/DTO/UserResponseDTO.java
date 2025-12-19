package com.pm.userservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private String role;
    private String committeeposition;
    private String salary;
    private String createdAt;

}
