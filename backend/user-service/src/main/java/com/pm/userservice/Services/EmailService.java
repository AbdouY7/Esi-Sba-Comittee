package com.pm.userservice.Services;


public interface EmailService {
    void sendVerificationEmail(String email , String username , String verificationToken);
    void sendPasswordResetEmail(String toEmail, String username, String resetToken);
}
