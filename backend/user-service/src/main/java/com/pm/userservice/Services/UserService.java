package com.pm.userservice.Services;

import com.pm.userservice.DTO.UserRequestDTO;
import com.pm.userservice.DTO.UserResponseDTO;
import com.pm.userservice.Entity.CommitteePosition;
import com.pm.userservice.Entity.Role;
import com.pm.userservice.Entity.User;
import com.pm.userservice.Exception.ExistingUserException;
import com.pm.userservice.Mappers.UserMapper;
import com.pm.userservice.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public List<UserResponseDTO> getAllusers() {
        List<UserResponseDTO> userResponseDTOS = new ArrayList<>();
        List<User> users = userRepository.findAll();
        return users.stream().map(UserMapper::toUserResponseDTO).toList();
    }
    public UserResponseDTO getUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ExistingUserException("this user is not existing");
        }
        User user = userRepository.getUserById(id);
        return UserMapper.toUserResponseDTO(user);
    }
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        if (userRequestDTO.getRole() == Role.COMMITTEE && userRequestDTO.getCommitteePosition() == null) {
            throw new IllegalArgumentException("commit position is required for committee members");
        }
        if (userRequestDTO.getRole() != Role.COMMITTEE && userRequestDTO.getCommitteePosition() != null ) {
            throw new IllegalArgumentException("Only committee members can have a committee position");
        }
        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }


        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        user.setPhone(userRequestDTO.getPhoneNumber());
        user.setRole(userRequestDTO.getRole());
        if (userRequestDTO.getRole() ==  Role.COMMITTEE ) {
            user.setCommitteePosition(userRequestDTO.getCommitteePosition());
        }else {user.setCommitteePosition(null);}
        user.setSalary(userRequestDTO.getSalary());
        userRepository.save(user);
        return UserMapper.toUserResponseDTO(user);
    }
}
