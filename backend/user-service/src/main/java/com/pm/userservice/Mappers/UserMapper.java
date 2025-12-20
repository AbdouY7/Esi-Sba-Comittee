package com.pm.userservice.Mappers;

import com.pm.userservice.DTO.UserRequestDTO;
import com.pm.userservice.DTO.UserResponseDTO;
import com.pm.userservice.Entity.User;
import org.springframework.stereotype.Component;


public record UserMapper() {

    public static  UserResponseDTO toUserResponseDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setPhoneNumber(user.getPhone());
        userResponseDTO.setRole(user.getRole().toString());
        userResponseDTO.setCommitteeposition(user.getCommitteePosition().toString());
        userResponseDTO.setSalary(user.getSalary().toString());
        userResponseDTO.setCreatedAt(user.getCreatedAt().toString());
        return userResponseDTO;
    }

    public static  User toUser(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());
        user.setPhone(userRequestDTO.getPhoneNumber());
        user.setRole(userRequestDTO.getRole());
        user.setCommitteePosition(userRequestDTO.getCommitteeposition());
        user.setSalary(userRequestDTO.getSalary());
        return user;
    }
}
