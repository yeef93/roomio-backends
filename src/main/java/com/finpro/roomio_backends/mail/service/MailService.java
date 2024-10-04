package com.finpro.roomio_backends.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class MailService {

    private JavaMailSender mailSender;
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.mail.displayName}")
    private String fromName;

    public void sendTestEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // Set to true for HTML content if needed
            helper.setFrom(fromEmail, fromName); // Set both email and display name

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Handle the exception as needed
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}

