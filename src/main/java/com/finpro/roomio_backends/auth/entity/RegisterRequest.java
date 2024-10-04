package com.finpro.roomio_backends.auth.entity;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String firstName;
    // Add other fields as necessary
}
