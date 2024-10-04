package com.finpro.roomio_backends.auth.service;

import com.finpro.roomio_backends.auth.entity.dto.login.LoginRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthService {

 String generateToken(Authentication authentication);

 ResponseEntity<?> login(LoginRequestDto loginRequestDTO);

 void logout();

}
