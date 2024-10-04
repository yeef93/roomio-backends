package com.finpro.roomio_backends.auth.entity.dto;

import lombok.Data;

@Data
public class ResetPasswordRequestDto {

    private String token;
    private String password;
    private String confirmPassword;

}
