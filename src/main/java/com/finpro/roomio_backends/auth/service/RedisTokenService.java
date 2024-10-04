package com.finpro.roomio_backends.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisTokenService {

    private final StringRedisTemplate redisTemplate;

    @Value("${verification.token.expiration:60}") // expiration time in minutes
    private long expirationTime;

    public RedisTokenService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void storeToken(String token, String value) {
        redisTemplate.opsForValue().set(token, value, expirationTime, TimeUnit.MINUTES);
    }

    public String getToken(String token) {
        return redisTemplate.opsForValue().get(token);
    }

    public void deleteToken(String token) {
        redisTemplate.delete(token);
    }

    public boolean isTokenValid(String token) {
        try {
            // Log the received token
            System.out.println("Checking token: " + token);

            // Retrieve the email associated with the token from Redis
            String email = redisTemplate.opsForValue().get(token);

            // Log the email retrieved from Redis
            System.out.println("Email from Redis: " + email);

            if (email == null) {
                // Token not found
                System.out.println("Token not found.");
                return false;
            }

            // Token exists and matches an email
            System.out.println("Token is valid.");
            return true;
        } catch (Exception e) {
            // Handle any unexpected exceptions
            e.printStackTrace();
            return false;
        }
    }
}