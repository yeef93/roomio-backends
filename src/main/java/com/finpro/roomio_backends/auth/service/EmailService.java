package com.finpro.roomio_backends.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;
    @Value("${spring.mail.displayName}")
    private String fromName;

    public void sendVerificationEmail(String to, String verificationLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("Verify your email");
            helper.setText("Click the link to verify your email: " + verificationLink);
            helper.setFrom(fromEmail, fromName); // Set both email and display name

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Handle the exception as needed
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendForgotPasswordEmail(String to, String verificationLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("Password Reset Request for Roomio");
//            helper.setText("Click the link to verify your email: " + verificationLink);
            helper.setFrom(fromEmail, fromName); // Set both email and display name

            // HTML content for the email
            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; }" +
                    ".container { padding: 20px; border: 1px solid #ddd; max-width: 600px; margin: auto; }" +
                    ".header { font-size: 20px; font-weight: bold; margin-bottom: 20px; }" +
                    ".content { font-size: 14px; margin-bottom: 20px; }" +
                    ".button { display: inline-block; padding: 10px 20px; background-color: #d9534f; color: #fff; text-decoration: none; font-size: 16px; border-radius: 5px; }" +
                    ".footer { font-size: 12px; color: #777; margin-top: 20px; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<div class='header'>Roomio - Password Reset Request</div>" +
                    "<p>Halo, </p>" +
                    "<p>Someone has requested a new password for the following account on Roomio:</p>" +
                    "<p>If you didn't make this request, just ignore this email. If you'd like to proceed:</p>" +
                    "<a href='" + verificationLink + "'>Click here to reset your password</a>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlContent, true); // Set the content type to HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Handle the exception as needed
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendChangeEmailConfirmation(String newEmail, String verificationLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(newEmail);
            helper.setSubject("Change Email Request for Roomio");
            helper.setFrom(fromEmail, fromName); // Set both email and display name

            // HTML content for the email
            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; }" +
                    ".container { padding: 20px; border: 1px solid #ddd; max-width: 600px; margin: auto; }" +
                    ".header { font-size: 20px; font-weight: bold; margin-bottom: 20px; }" +
                    ".content { font-size: 14px; margin-bottom: 20px; }" +
                    ".button { display: inline-block; padding: 10px 20px; background-color: #d9534f; color: #fff; text-decoration: none; font-size: 16px; border-radius: 5px; }" +
                    ".footer { font-size: 12px; color: #777; margin-top: 20px; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<div class='header'>Roomio - Change Email Request</div>" +
                    "<p>Halo, </p>" +
                    "<p>Someone has requested a change email for the account on Roomio:</p>" +
                    "<p>If you didn't make this request, just ignore this email. If you'd like to proceed:</p>" +
                    "<a href='" + verificationLink + "'>Click here to change your email</a>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlContent, true); // Set the content type to HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Handle the exception as needed
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}