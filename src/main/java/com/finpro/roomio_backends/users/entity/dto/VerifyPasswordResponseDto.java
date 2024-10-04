package com.finpro.roomio_backends.users.entity.dto;

import lombok.Data;

@Data
public class VerifyPasswordResponseDto {
    private boolean success;

    public VerifyPasswordResponseDto(boolean success){
        this.success = success;
    }
}
