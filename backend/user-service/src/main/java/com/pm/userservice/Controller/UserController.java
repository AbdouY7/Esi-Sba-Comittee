package com.pm.userservice.Controller;

import com.pm.userservice.DTO.UserRequestDTO;
import com.pm.userservice.DTO.UserResponseDTO;
import com.pm.userservice.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user_api")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/all_users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> userResponseDTOList = new ArrayList<>();
        userResponseDTOList = userService.getAllusers();
        return ResponseEntity.ok().body(userResponseDTOList);
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO = userService.getUserById(id);
        return ResponseEntity.ok().body(userResponseDTO);
    }

    @PostMapping("/create_user")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO = userService.createUser(userRequestDTO);
        return ResponseEntity.ok().body(userResponseDTO);
    }
}
