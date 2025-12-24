package com.pm.userservice.DTO;

import com.pm.userservice.Entity.CommitteePosition;
import com.pm.userservice.Entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequestDTO {

    private String username;

    @Email(message = "Email should be valid")
    private String email;

    private String phoneNumber;

    private Role role;

    private CommitteePosition committeePosition;

    private Double salary;

    private boolean isActive;
}
