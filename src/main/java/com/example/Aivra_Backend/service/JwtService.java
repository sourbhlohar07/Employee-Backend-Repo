package com.example.Aivra_Backend.service;
import com.example.Aivra_Backend.entity.AdminDetail;

public interface JwtService {

    String generateAccessToken(AdminDetail admin);

    long getAccessTokenLifetimeSeconds();
}
