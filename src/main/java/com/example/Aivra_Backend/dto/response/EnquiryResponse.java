package com.example.Aivra_Backend.dto.response;

import com.example.Aivra_Backend.enums.EnquiryStatus;

import java.time.LocalDateTime;

public record EnquiryResponse(
        Long id,
        String name,
        String email,
        String phone,
        String message,
        String city,
        EnquiryStatus status,
        LocalDateTime createdAt
) {
}
