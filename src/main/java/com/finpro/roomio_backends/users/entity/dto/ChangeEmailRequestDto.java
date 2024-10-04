package com.finpro.roomio_backends.users.entity.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeEmailRequestDto {

    @NotNull
    @NotEmpty
    private String currentPassword;
    @NotNull
    @NotEmpty
    private String newEmail;
}
