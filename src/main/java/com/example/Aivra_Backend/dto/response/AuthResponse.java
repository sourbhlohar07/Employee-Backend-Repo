package com.example.Aivra_Backend.dto.response;

public record AuthResponse(
        String message,
        String accessToken,
        String tokenType,
        long expiresIn,
        Long adminId,
        String name,
        String email,
        String role
) {}
