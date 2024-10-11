package com.finpro.roomio_backends.users.service.impl;

import com.finpro.roomio_backends.auth.service.EmailService;
import com.finpro.roomio_backends.auth.service.RedisTokenService;
import com.finpro.roomio_backends.exceptions.image.ImageNotFoundException;
import com.finpro.roomio_backends.exceptions.user.UserNotFoundException;
import com.finpro.roomio_backends.image.entity.ImageUserAvatar;
import com.finpro.roomio_backends.image.entity.dto.ImageUploadRequestDto;
import com.finpro.roomio_backends.image.service.ImageService;
import com.finpro.roomio_backends.users.entity.Users;
import com.finpro.roomio_backends.users.entity.dto.ChangeEmailRequestDto;
import com.finpro.roomio_backends.users.entity.dto.UserProfileDto;
import com.finpro.roomio_backends.users.entity.dto.changePassword.ChangePasswordRequestDto;
import com.finpro.roomio_backends.users.entity.dto.userManagement.ProfileUpdateRequestDTO;
import com.finpro.roomio_backends.users.repository.UsersRepository;
import com.finpro.roomio_backends.users.service.UsersService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final RedisTokenService redisTokenService;
    private final EmailService emailService;

    public UsersServiceImpl(UsersRepository userRepository, PasswordEncoder passwordEncoder,
                            ImageService imageService, RedisTokenService redisTokenService,
                            EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
        this.redisTokenService = redisTokenService;
        this.emailService = emailService;
    }

    @Value("${app.verification-url}verify-newemail")
    private String verificationUrl;


    @Override
    public Optional<Users> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Users getByUsername(String email) throws RuntimeException {
        Optional<Users> usersOptional = userRepository.findByEmail(email);
        return usersOptional.orElseThrow(() -> new UserNotFoundException(
                "User by email: " + email + " not found. Please ensure you've entered the correct email!"));
    }

    @Override
    public Users getCurrentUser() throws RuntimeException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AccessDeniedException("You must be logged in to access this resource");
        }
        String username = authentication.getName();
        return getByUsername(username);
    }

    @Override
    @Transactional
    public UserProfileDto getProfile() throws RuntimeException {
        Users user = getCurrentUser();
        return new UserProfileDto(user);
    }

    @Override
    public boolean verifyPassword(String email, String rawPassword) {
        return userRepository.findByEmail(email)
                .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .orElse(false);
    }

    @Override
    public ImageUserAvatar uploadAvatar(ImageUploadRequestDto requestDto) throws IllegalArgumentException {
        Users user = getCurrentUser();
        return imageService.uploadAvatar(requestDto, user);
    }

    @Override
    public String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername(); // Username is typically the email
        } else {
            return principal.toString();
        }
    }

    @Override
    public void changePassword(ChangePasswordRequestDto requestDto) throws RuntimeException {
        Users loggedInUser = getCurrentUser();

        // Verify old password
        if (!passwordEncoder.matches(requestDto.getOldPassword(), loggedInUser.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        // Check if new password matches confirm password
        if (!requestDto.getNewPassword().equals(requestDto.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }

        // Check if new password is the same as the old password
        if (passwordEncoder.matches(requestDto.getNewPassword(), loggedInUser.getPassword())) {
            throw new IllegalArgumentException("New password cannot be the same as the old password");
        }

        // Update password
        loggedInUser.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
        userRepository.save(loggedInUser);
    }

    @Override
    public void update(ProfileUpdateRequestDTO requestDto)
            throws RuntimeException {
        Users existingUser = getCurrentUser();
        ProfileUpdateRequestDTO update = new ProfileUpdateRequestDTO();
        update.dtoToEntity(existingUser, requestDto);

        // check for image
        if (requestDto.getAvatarId() != null) {
            ImageUserAvatar avatar = imageService.getAvatarById(requestDto.getAvatarId());
            if (avatar != null) {
                existingUser.setAvatar(avatar);
            } else {
                throw new ImageNotFoundException(
                        "ImageUserAvatar doesn't exist in database. Please enter another imageId or upload a "
                                + "new image");
            }
        }
        userRepository.save(existingUser);
    }

    public boolean isEmailRegistered(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public void changeEmail(ChangeEmailRequestDto requestDto) throws RuntimeException {
        Users loggedInUser = getCurrentUser();
        // Verify password
        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), loggedInUser.getPassword())) {
            throw new IllegalArgumentException("Password is incorrect");
        }

        // Check if the new email is already registered
        if (isEmailRegistered(requestDto.getNewEmail())) {
            throw new IllegalArgumentException("New email is already registered.");
        }

        // Send a confirmation email for the new email
        String token = UUID.randomUUID().toString();
        redisTokenService.storeToken(token, requestDto.getNewEmail());

        // Generate the verification link
        String verificationLink = verificationUrl + "?token=" + token;

        // Send email
        emailService.sendChangeEmailConfirmation(requestDto.getNewEmail(), verificationLink);
    }

    // Validate current email and password
    @Override
    public boolean validateUserCredentials(String currentEmail, String currentPassword) {
        // Fetch the user by current email
        Users user = userRepository.findByEmail(currentEmail).orElseThrow(() -> new RuntimeException("User not found"));

        // Validate password
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }

    // Check if the new email is available (not already used by another user)
    @Override
    public boolean isEmailAvailable(String newEmail) {
        return userRepository.findByEmail(newEmail).isEmpty();
    }

    // Update user's email in the database
    @Override
    public void changeUserEmail(String currentEmail, String newEmail) {
        Users user = userRepository.findByEmail(currentEmail).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEmail(newEmail);
        userRepository.save(user);
    }


    @Override
    public Users saveOrUpdateUser(String email, String name, String provider, Boolean isTenant) {
        Optional<Users> existingUser = userRepository.findByEmail(email);
        Users user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
            user.setFirstname(name);
        } else {
            user = new Users();
            user.setEmail(email);
            user.setFirstname(name);
            user.setMethod(provider);
            user.setIsTenant(isTenant);
            user.setIsVerified(true);
        }
        return userRepository.save(user);
    }

}
