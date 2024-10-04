package com.finpro.roomio_backends.users.service;

import com.finpro.roomio_backends.image.entity.ImageUserAvatar;
import com.finpro.roomio_backends.image.entity.dto.ImageUploadRequestDto;
import com.finpro.roomio_backends.users.entity.Users;
import com.finpro.roomio_backends.users.entity.dto.ChangeEmailRequestDto;
import com.finpro.roomio_backends.users.entity.dto.UserProfileDto;
import com.finpro.roomio_backends.users.entity.dto.changePassword.ChangePasswordRequestDto;
import com.finpro.roomio_backends.users.entity.dto.userManagement.ProfileUpdateRequestDTO;

import java.util.Optional;

public interface UsersService {
    Optional<Users> getUserByEmail(String email);

    Users getByUsername(String username);

    // getting logged-in user
    Users getCurrentUser();

    // Getting Users
    UserProfileDto getProfile();

    boolean verifyPassword(String email, String rawPassword);

    // uploading picture per user
    ImageUserAvatar uploadAvatar(ImageUploadRequestDto requestDto);

    String getCurrentUserEmail();

    void changePassword(ChangePasswordRequestDto requestDto);

    void update(ProfileUpdateRequestDTO requestDto);

    boolean isEmailRegistered(String email);

    void changeEmail(ChangeEmailRequestDto requestDto);

    boolean validateUserCredentials(String currentEmail, String currentPassword);

    boolean isEmailAvailable(String newEmail);

    void changeUserEmail(String currentEmail, String newEmail);



}
