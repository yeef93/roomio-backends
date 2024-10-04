package com.finpro.roomio_backends.users.entity.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class VerifyPasswordRequestDto {
    @NotEmpty(message = "Password must not be empty")
    private String password;
}
