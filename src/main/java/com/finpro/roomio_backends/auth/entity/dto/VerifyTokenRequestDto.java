package com.finpro.roomio_backends.auth.entity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyTokenRequestDto {

    @NotNull(message = "Token must not be null")
    private String token;
}
