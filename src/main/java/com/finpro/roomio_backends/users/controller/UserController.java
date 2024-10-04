package com.finpro.roomio_backends.users.controller;

import com.finpro.roomio_backends.auth.service.EmailService;
import com.finpro.roomio_backends.auth.service.RedisTokenService;
import com.finpro.roomio_backends.exceptions.image.ImageNotFoundException;
import com.finpro.roomio_backends.image.entity.ImageUserAvatar;
import com.finpro.roomio_backends.image.entity.dto.AvatarImageResponseDto;
import com.finpro.roomio_backends.image.entity.dto.ImageUploadRequestDto;
import com.finpro.roomio_backends.responses.Response;
import com.finpro.roomio_backends.users.entity.dto.ChangeEmailRequestDto;
import com.finpro.roomio_backends.users.entity.dto.UserProfileDto;
import com.finpro.roomio_backends.users.entity.dto.VerifyPasswordRequestDto;
import com.finpro.roomio_backends.users.entity.dto.VerifyPasswordResponseDto;
import com.finpro.roomio_backends.users.entity.dto.changePassword.ChangePasswordRequestDto;
import com.finpro.roomio_backends.users.entity.dto.userManagement.ProfileUpdateRequestDTO;
import com.finpro.roomio_backends.users.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UsersService userService;
    private final RedisTokenService redisTokenService;
    private final EmailService emailService;

    public UserController(UsersService userService,
                          RedisTokenService redisTokenService,
                          EmailService emailService) {
        this.userService = userService;
        this.redisTokenService = redisTokenService;
        this.emailService = emailService;
    }

    // * Get logged in user's profile
    @GetMapping("/me")
    public ResponseEntity<Response<UserProfileDto>> getUserProfile() {
        try {
            UserProfileDto userProfile = userService.getProfile();
            if (userProfile != null) {
                return Response.successfulResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), userProfile);
            } else {
                return Response.failedResponse(HttpStatus.NOT_FOUND.value(), "There is no user profile", null);
            }
        } catch (Exception e) {
            return Response.failedResponse(HttpStatus.BAD_REQUEST.value(), "Failed to get user data: " + e.getMessage());
        }
    }

    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@RequestBody VerifyPasswordRequestDto request) {
        try {
            String currentUserEmail = userService.getCurrentUserEmail();
            boolean isPasswordValid = userService.verifyPassword(currentUserEmail, request.getPassword());
            return Response.successfulResponse(HttpStatus.OK.value(), "Password successfull verified", new VerifyPasswordResponseDto(isPasswordValid));
        } catch (Exception e) {
            return Response.failedResponse(HttpStatus.BAD_REQUEST.value(), "Failed to verify password: " + e.getMessage());
        }
    }

    // * upload image
    @PostMapping("/me/image/upload")
    public ResponseEntity<Response<AvatarImageResponseDto>> uploadImage(ImageUploadRequestDto requestDto) {
        try {
            ImageUserAvatar uploadedImageUserAvatar = userService.uploadAvatar(requestDto);
            if (uploadedImageUserAvatar == null) {
                return ResponseEntity.noContent().build();
            } else {
                return Response.successfulResponse(HttpStatus.OK.value(), "Image success uploaded!", new AvatarImageResponseDto(
                        uploadedImageUserAvatar));
            }
        } catch (Exception e) {
            return Response.failedResponse(HttpStatus.BAD_REQUEST.value(), "Failed to upload image: " + e.getMessage());
        }
    }

    // * Edit Profile
    @PutMapping("/me")
    public ResponseEntity<Response<UserProfileDto>> updateUserProfile(@Valid @RequestBody ProfileUpdateRequestDTO requestDTO)
            throws ImageNotFoundException {
        try {
            userService.update(requestDTO);
            UserProfileDto userProfile = userService.getProfile();
            return Response.successfulResponse(HttpStatus.OK.value(), "Profile update successful!! :D", userProfile);
        } catch (Exception e) {
            return Response.failedResponse(HttpStatus.BAD_REQUEST.value(), "Failed to update profile: " + e.getMessage());
        }
    }

    // * Change Password
    @PutMapping("me/change-password")
    public ResponseEntity<Response<Void>> changePassword(@Valid @RequestBody ChangePasswordRequestDto requestDto) {
        try {
            userService.changePassword(requestDto);
            return Response.successfulResponse(HttpStatus.OK.value(),"Password change successful!", null);
        } catch (Exception e) {
            return Response.failedResponse(HttpStatus.BAD_REQUEST.value(), "Failed to change password: " + e.getMessage());
        }
    }

    // * Change Email
    @PostMapping("me/change-email")
    public ResponseEntity<?> changeEmail(@RequestBody ChangeEmailRequestDto requestDto) {
        try {
            userService.changeEmail(requestDto);
            // Return success response
            return Response.successfulResponse(HttpStatus.OK.value(), "Email change request successful. Please check your new email for confirmation.", null);

        } catch (Exception e) {
            return Response.failedResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to process email change: " + e.getMessage());
        }
    }


}