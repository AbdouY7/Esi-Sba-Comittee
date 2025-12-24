package com.pm.userservice.Controller;

import com.pm.userservice.DTO.*;
import com.pm.userservice.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> completeRegisterUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        try{
            UserResponseDTO userResponseDTO = userService.completeRegistration(registrationDTO);
            return ResponseEntity.ok().body(userResponseDTO);
        }catch (Exception e){
            return ResponseEntity.badRequest().body((UserResponseDTO) Map.of("error" , e.getMessage()));
        }
    }

    @PostMapping("/verify-mail")
    public ResponseEntity<VerificationResponseDTO> verifyMail(@Valid @RequestBody EmailVerificationTokenDTO verificationDTO) {
        try {
            VerificationResponseDTO response = userService.verifyEmail(verificationDTO);
            return ResponseEntity.ok().body(response);

        }catch (Exception e){
            return ResponseEntity.badRequest().body((VerificationResponseDTO) Map.of("error" , e.getMessage()));
        }
    }

    @PostMapping("/resend-verify-mail")
    public ResponseEntity<?> resendVerificationMail(@Valid @RequestBody Map<String , String> request) {
        try{
            String email = request.get("email");
            userService.resendVerificationEmail(email);
            return ResponseEntity.ok().body(Map.of("message" , "Verification email sent. please check it out"));

        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body((VerificationResponseDTO) Map.of("error" , e.getMessage()));
        }
    }

    @GetMapping("/can-login/{email}")
    public ResponseEntity<Boolean> getUserByEmail(@PathVariable String email) {
        boolean response = userService.canUserLogin(email);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id ,@Valid @RequestBody UpdateRequestDTO updateRequestDTO) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO = userService.updateUser(id, updateRequestDTO);
        return ResponseEntity.ok().body(userResponseDTO);
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().body(true);
    }
}
