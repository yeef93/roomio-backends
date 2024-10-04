package com.finpro.roomio_backends.users.entity.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VerifyChangeEmailRequestDto {

    @NotNull
    @NotEmpty
    private String token;
    @NotNull
    @NotEmpty
    private String currentEmail;
    @NotNull
    @NotEmpty
    private String currentPassword;
}
