
package com.example.Aivra_Backend.service;


import com.example.Aivra_Backend.dto.request.LoginRequest;
import com.example.Aivra_Backend.dto.response.AuthResponse;

public interface AdminAuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(String refreshToken);

    void logout(String refreshToken);
}
