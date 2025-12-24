package com.pm.userservice.Services;

import com.pm.userservice.DTO.*;
import com.pm.userservice.Entity.Role;
import com.pm.userservice.Entity.User;
import com.pm.userservice.Exception.ExistingUserException;
import com.pm.userservice.Mappers.UserMapper;
import com.pm.userservice.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Data
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;


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
        user.setActive(false);
        user.setEmailIsVerified(false);
        user.setPassword(null);
        userRepository.save(user);

//        String newToken = UUID.randomUUID().toString();
//        emailService.sendVerificationEmail(user.getEmail(), user.getUsername(), newToken);

        return UserMapper.toUserResponseDTO(user);
    }

    // the registration function
    public UserResponseDTO completeRegistration(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByEmail(registrationDTO.getEmail()) && userRepository.findUserByEmail(registrationDTO.getEmail()).get().isActive()) {
            throw new IllegalArgumentException("user is  already exists and registred , go and login directly");
        }
        if (!registrationDTO.getPassword().equals(registrationDTO.getComfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = new User();
        user = userRepository.findUserByEmail(registrationDTO.getEmail()).orElseThrow(() -> new IllegalArgumentException("User not found. Please contact admin."));
        user.setPassword(registrationDTO.getPassword());
        user.setActive(true);
        user.setEmailIsVerified(false);

        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setTokenExpiryDate(LocalDateTime.now().plusHours(24));

        User updatedUser = userRepository.save(user);

        emailService.sendVerificationEmail(
                updatedUser.getEmail(),
                updatedUser.getUsername(),
                verificationToken
        );

        return UserMapper.toUserResponseDTO(updatedUser) ;
    }

    // function to verify the email
    public VerificationResponseDTO verifyEmail(EmailVerificationTokenDTO verificationDTO) {
        User user = userRepository.findUserByVerificationToken(verificationDTO.getToken());

        if (user.getTokenExpiryDate() != null && LocalDateTime.now().isAfter(user.getTokenExpiryDate())) {
            throw new IllegalArgumentException("token is expired");
        }
        if (user.isEmailIsVerified()){
            return new VerificationResponseDTO(
                    true ,
                    "Email already verified. You can now login.",
                    user.getEmail()
            );
        }
        user.setEmailIsVerified(true);
        user.setVerificationToken(null);
        user.setTokenExpiryDate(null);
        userRepository.save(user);

        return new VerificationResponseDTO(
                true ,
                "Email verified successfully! You can now login to your account.",
                user.getEmail()
        );


    }

    // function to resend the verification email

    public void resendVerificationEmail(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if user has set password


        // Check if already verified
        if (user.isEmailIsVerified()) {
            throw new IllegalArgumentException("Email already verified");
        }

        // Generate new token
        String newToken = UUID.randomUUID().toString();
        user.setVerificationToken(newToken);
        user.setTokenExpiryDate(LocalDateTime.now().plusHours(24));

        userRepository.save(user);

        // Resend email
        emailService.sendVerificationEmail(
                user.getEmail(),
                user.getUsername(),
                newToken
        );
    }

    public boolean canUserLogin(String email) {
        Optional<User> userOpt = userRepository.findUserByEmail(email);
        if (userOpt.isEmpty()) {
            return false;
        }
        User  user = userOpt.get();
        return user.isActive() && user.isEmailIsVerified() && user.getPassword() != null;

    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public UserResponseDTO updateUser( Long userId , UpdateRequestDTO updateRequestDTO) {
        User updateUser = userRepository.getUserById(userId);
        if (updateRequestDTO.getUsername() != null && !updateRequestDTO.getUsername().equals(updateUser.getUsername())) {
            updateUser.setUsername(updateRequestDTO.getUsername());
        }
        if (updateRequestDTO.getEmail() != null && !updateRequestDTO.getEmail().trim().isEmpty()) {
            User existingUser = userRepository.findUserByEmail(updateRequestDTO.getEmail()).orElseThrow(() -> new IllegalArgumentException("User not found"));
            if (existingUser != null && existingUser.getId().equals(userId)) {
                throw new IllegalArgumentException("Email already exists");
            }else {
                updateUser.setEmailIsVerified(false);
                updateUser.setEmail(updateRequestDTO.getEmail());
                //TODO send validation email
            }
        }
        if (updateRequestDTO.getPhoneNumber() != null && !updateRequestDTO.getPhoneNumber().trim().isEmpty()) {
            updateUser.setPhone(updateRequestDTO.getPhoneNumber());
        }

        // Update role if provided
        if (updateRequestDTO.getRole() != null) {
            updateUser.setRole(updateRequestDTO.getRole());

            // If role changed to non-COMMITTEE, remove committee position
            if (updateRequestDTO.getRole() != Role.COMMITTEE) {
                updateUser.setCommitteePosition(null);
            }
        }

        // Update committee position if provided
        if (updateRequestDTO.getCommitteePosition() != null) {
            // Validate: only COMMITTEE members can have a position
            if (updateUser.getRole() != Role.COMMITTEE) {
                throw new IllegalArgumentException("Only committee members can have a committee position");
            }
            updateUser.setCommitteePosition(updateRequestDTO.getCommitteePosition());
        } else if (updateRequestDTO.getRole() == Role.COMMITTEE && updateUser.getCommitteePosition() == null) {
            // If changing to COMMITTEE but no position provided, require it
            throw new IllegalArgumentException("Committee members must have a committee position");
        }

        // Update salary if provided
        if (updateRequestDTO.getSalary() != null) {
            updateUser.setSalary(updateRequestDTO.getSalary());
        }

        // Update active status if provided
        if (updateRequestDTO.isActive()) {
            updateUser.setActive(true);
        }

        // Save updated user
        User updatedUser = userRepository.save(updateUser);
        return UserMapper.toUserResponseDTO(updatedUser);
    }

    public void deleteUser(Long userId) {
        User deletedUser = userRepository.getUserById(userId);
        if (deletedUser == null) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(userId);
    }


}
