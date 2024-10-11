package com.finpro.roomio_backends.auth.service;

import com.finpro.roomio_backends.auth.entity.dto.login.LoginRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface AuthService {

 String generateToken(Authentication authentication);

 ResponseEntity<?> login(LoginRequestDto loginRequestDTO);

 void logout();

 ResponseEntity<?> loginWithGoogle(Map<String, String> userData);

}
