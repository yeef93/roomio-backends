package com.finpro.roomio_backends.auth.service;


import com.finpro.roomio_backends.image.entity.ImageUserAvatar;
import com.finpro.roomio_backends.users.entity.Users;
import com.finpro.roomio_backends.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UsersRepository userRepository;
    private final EmailService emailService;
    private final RedisTokenService redisTokenService;

    @Value("${app.verification-url}verify-page")
    private String verificationUrl;

    @Value("${app.verification-url}reset-password")
    private String resetPasswordUrl;

    @Transactional
    public void registerUser(String email) {
        String token = UUID.randomUUID().toString();

        // Check if email already exists
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new IllegalArgumentException("Email already registered");
        });

        String firstname = generateFirstname(email);

        Users user = new Users();
        user.setEmail(email);
        user.setMethod("email");
        user.setIsVerified(false);
        user.setFirstname(firstname);

        // Set the avatar with ID 1
        ImageUserAvatar avatar = new ImageUserAvatar();
        avatar.setId(1L); // assuming ID is of type Long
        user.setAvatar(avatar);

        redisTokenService.storeToken(token, email);

        try {
            // Attempt to save the new user
            userRepository.save(user);

            // Only send the verification email if the user is successfully saved
            String verificationLink = verificationUrl + "?token=" + token;
            emailService.sendVerificationEmail(email, verificationLink);

        } catch (Exception e) {
            // Handle the case where user saving fails
            // Log the exception or perform other actions if needed
            throw new RuntimeException("Failed to register user", e);
        }
    }

    // Method to generate a firstname
    private String generateFirstname(String email) {
        // Extract part of the email (before @)
        String emailName = email.substring(0, email.indexOf("@"));

        // get emailname
        return emailName;
    }

    @Transactional
    public void resendVerificationEmail(String email) {
        // Find the user by email
        Optional<Users> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Email not registered");
        }

        Users user = userOptional.get();

        // Check if the user is already verified
        if (user.getIsVerified()== true) {
            throw new IllegalStateException("User is already verified");
        }

        // Generate a new verification token
        String newToken = UUID.randomUUID().toString();
        redisTokenService.storeToken(newToken, email);
        //user.setVerificationToken(newToken);

        // Send the verification email
        String verificationLink = verificationUrl + "?token=" + newToken;
        emailService.sendVerificationEmail(email, verificationLink);
    }


    @Transactional
    public void verifyUser(String email, String hashedPassword) {
        Logger logger = LoggerFactory.getLogger(getClass());

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setPassword(hashedPassword);
        user.setIsVerified(true);

        userRepository.save(user);
        logger.info("User with email {} has been verified and password updated.", email);
    }

    @Transactional
    public void registerTenant(String email) {
        String token = UUID.randomUUID().toString();
        // Check if email already exists
        userRepository.findByEmail(email).ifPresent(user -> {
            if(user.getIsTenant()== true){
                throw new IllegalArgumentException("Email already registered as tenant");
            }
            else {
                throw new IllegalArgumentException("Email already registered as user");
            }

        });

        String firstname = generateFirstname(email);

        Users user = new Users();
        user.setEmail(email);
        user.setMethod("email");
        user.setIsTenant(true);
        user.setIsVerified(false);
        user.setFirstname(firstname);
        // Set the avatar with ID 1
        ImageUserAvatar avatar = new ImageUserAvatar();
        avatar.setId(1L); // assuming ID is of type Long
        user.setAvatar(avatar);

        redisTokenService.storeToken(token, email);

        try {
            // Attempt to save the new user
            userRepository.save(user);

            // Only send the verification email if the user is successfully saved
            String verificationLink = verificationUrl + "?token=" + token;
            emailService.sendVerificationEmail(email, verificationLink);

        } catch (Exception e) {
            // Handle the case where user saving fails
            // Log the exception or perform other actions if needed
            throw new RuntimeException("Failed to register user", e);
        }
    }

    public Users findUserByEmail(String email) {
        // Assuming UserRepository has a method to find a user by email
        return userRepository.findByEmail(email)
                .orElse(null); // Return null if user not found
    }

    @Transactional
    public void forgotPassword(String email) {
        String token = UUID.randomUUID().toString();

        // Check if email is not registered
        if (userRepository.findByEmail(email).isEmpty()) {
            throw new IllegalArgumentException("Email not registered");
        }

        redisTokenService.storeToken(token, email);

        try {
            // Send the forgot password
            String forgotLink = resetPasswordUrl + "?token=" + token;
            emailService.sendForgotPasswordEmail(email, forgotLink);

        } catch (Exception e) {
            // Handle the case where user saving fails
            // Log the exception or perform other actions if needed
            throw new RuntimeException("Failed to send reset password mail", e);
        }
    }

}