package com.finpro.roomio_backends.auth.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CheckEmailDto {
    @Email(message = "Must be a well-formed email address")
    @NotEmpty(message = "Email must not be empty")
    private String email;
}
