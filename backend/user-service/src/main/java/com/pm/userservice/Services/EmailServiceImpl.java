package com.pm.userservice.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    JavaMailSender mailSender;

    @Value("${app.base.url}")
    private String urlBased;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @SneakyThrows
    @Override
    public void sendVerificationEmail(String email , String username , String verificationToken) {
        try{String verificationLink = "http://localhost:3000/verify-email?token=" + verificationToken;
            String emailContent = buildVerificationEmailContent(username , verificationLink);

            System.out.println("=== EMAIL VERIFICATION ===");
            System.out.println("To: " + email);
            System.out.println("Subject: Verify Your Email - ESI SBA Committee Platform");
            System.out.println("\nHi " + username + ",");
            System.out.println("\nWelcome to ESI SBA Committee Platform!");
            System.out.println("Please verify your email by clicking the link below:");
            System.out.println("\n" + verificationLink);
            System.out.println("\nThis link will expire in 24 hours.");
            System.out.println("\nIf you didn't request this, please ignore this email.");
            System.out.println("========================\n");


            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = null;
            try {
                helper = new MimeMessageHelper(message, true);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            try {
                helper.setTo(email);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            helper.setFrom(fromEmail , "ESI SBA Committee Platform");
            helper.setSubject("Verify Your Email");
            helper.setText(emailContent, true);
            mailSender.send(message);
        }catch (MessagingException e){
            System.err.println("❌ Failed to send verification email to: " + email);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Unexpected error sending email to: " + email);
            e.printStackTrace();
            throw new RuntimeException("Failed to send verification email", e);
        }

    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String username, String resetToken) {
        try{
            String resetLink = "http://localhost:3000/reset-email?token=" + resetToken;
            String emailContent =  buildPasswordResetEmailContent(username, resetLink);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message , true , "UTF-8");

            helper.setFrom(fromEmail ,"ESI SBA Committee Platform" );
            helper.setTo(toEmail);
            helper.setSubject("Reset Password");
            helper.setText(emailContent, true);
            mailSender.send(message);

            System.out.println("✅ Password reset email sent to: " + toEmail);
        }catch (MailException | MessagingException | UnsupportedEncodingException e){
            System.err.println("❌ Failed to send password reset email to: " + toEmail);
            e.printStackTrace();
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    private String buildVerificationEmailContent(String username, String verificationLink) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "        .header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }" +
                "        .content { padding: 20px; background-color: #f9f9f9; }" +
                "        .button { display: inline-block; padding: 12px 30px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }" +
                "        .footer { padding: 20px; text-align: center; font-size: 12px; color: #777; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>" +
                "            <h1>Welcome to ESI SBA Committee Platform!</h1>" +
                "        </div>" +
                "        <div class='content'>" +
                "            <h2>Hi " + username + ",</h2>" +
                "            <p>Thank you for completing your registration!</p>" +
                "            <p>Please verify your email address by clicking the button below:</p>" +
                "            <p style='text-align: center;'>" +
                "                <a href='" + verificationLink + "' class='button'>Verify Email</a>" +
                "            </p>" +
                "            <p>Or copy and paste this link into your browser:</p>" +
                "            <p style='word-break: break-all; color: #4CAF50;'>" + verificationLink + "</p>" +
                "            <p><strong>This link will expire in 24 hours.</strong></p>" +
                "            <p>If you didn't create an account, please ignore this email.</p>" +
                "        </div>" +
                "        <div class='footer'>" +
                "            <p>© 2025 ESI SBA Committee Platform. All rights reserved.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    private String buildPasswordResetEmailContent(String username, String resetLink) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "        .header { background-color: #f44336; color: white; padding: 20px; text-align: center; }" +
                "        .content { padding: 20px; background-color: #f9f9f9; }" +
                "        .button { display: inline-block; padding: 12px 30px; background-color: #f44336; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }" +
                "        .footer { padding: 20px; text-align: center; font-size: 12px; color: #777; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>" +
                "            <h1>Password Reset Request</h1>" +
                "        </div>" +
                "        <div class='content'>" +
                "            <h2>Hi " + username + ",</h2>" +
                "            <p>We received a request to reset your password.</p>" +
                "            <p>Click the button below to reset your password:</p>" +
                "            <p style='text-align: center;'>" +
                "                <a href='" + resetLink + "' class='button'>Reset Password</a>" +
                "            </p>" +
                "            <p>Or copy and paste this link into your browser:</p>" +
                "            <p style='word-break: break-all; color: #f44336;'>" + resetLink + "</p>" +
                "            <p><strong>This link will expire in 1 hour.</strong></p>" +
                "            <p>If you didn't request a password reset, please ignore this email.</p>" +
                "        </div>" +
                "        <div class='footer'>" +
                "            <p>© 2025 ESI SBA Committee Platform. All rights reserved.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }


}
