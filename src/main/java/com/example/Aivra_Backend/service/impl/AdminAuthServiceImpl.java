
package com.example.Aivra_Backend.service.impl;


import com.example.Aivra_Backend.dto.request.LoginRequest;
import com.example.Aivra_Backend.dto.response.AuthResponse;
import com.example.Aivra_Backend.entity.AdminDetail;
import com.example.Aivra_Backend.entity.RefreshToken;
import com.example.Aivra_Backend.repositry.AdminRepository;
import com.example.Aivra_Backend.repositry.RefreshTokenRepository;
import com.example.Aivra_Backend.service.AdminAuthService;
import com.example.Aivra_Backend.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;

@Service
public class AdminAuthServiceImpl implements AdminAuthService {

    private final AdminRepository adminRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final long refreshDays;

    private final SecureRandom secureRandom = new SecureRandom();

    public AdminAuthServiceImpl(
            AdminRepository adminRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            @Value("${app.jwt.refresh-days}") long refreshDays) {

        this.adminRepository = adminRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshDays = refreshDays;
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {

        AdminDetail admin = adminRepository
                .findByEmailIgnoreCase(request.getEmail().trim())
                .orElseThrow(this::invalidCredentials);

        if (!passwordEncoder.matches(
                request.getPassword(), admin.getPassword())) {
            throw invalidCredentials();
        }

        if (!"ACTIVE".equalsIgnoreCase(admin.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Admin account is inactive");
        }

        if (!"ADMIN".equalsIgnoreCase(admin.getRole())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied");
        }

        String accessToken = jwtService.generateAccessToken(admin);
        String refreshToken = createRefreshToken(admin);

        return new AuthResponse(
                "Login successful",
                accessToken,
                "Bearer",
                jwtService.getAccessTokenLifetimeSeconds(),
                admin.getId(),
                admin.getName(),
                admin.getEmail(),
                admin.getRole()
        );
    }

    @Override
    @Transactional
    public AuthResponse refresh(String rawRefreshToken) {

        if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
            throw invalidRefreshToken();
        }

        String hash = hashToken(rawRefreshToken);

        RefreshToken existing = refreshTokenRepository
                .findByTokenHash(hash)
                .orElseThrow(this::invalidRefreshToken);

        if (existing.isRevoked() || existing.isExpired()) {
            throw invalidRefreshToken();
        }

        AdminDetail admin = existing.getAdmin();

        if (!"ACTIVE".equalsIgnoreCase(admin.getStatus())
                || !"ADMIN".equalsIgnoreCase(admin.getRole())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Admin account is inactive");
        }

        // Revoke the previous token before issuing a replacement.
        existing.revoke();

        String newAccessToken = jwtService.generateAccessToken(admin);
        createRefreshToken(admin);

        return new AuthResponse(
                "Token refreshed successfully",
                newAccessToken,
                "Bearer",
                jwtService.getAccessTokenLifetimeSeconds(),
                admin.getId(),
                admin.getName(),
                admin.getEmail(),
                admin.getRole()
        );
    }

    @Override
    @Transactional
    public void logout(String rawRefreshToken) {

        if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
            return;
        }

        refreshTokenRepository
                .findByTokenHash(hashToken(rawRefreshToken))
                .ifPresent(RefreshToken::revoke);
    }

    private String createRefreshToken(AdminDetail admin) {

        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);

        String rawToken = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);

        RefreshToken entity = new RefreshToken(
                admin,
                hashToken(rawToken),
                Instant.now().plus(refreshDays, ChronoUnit.DAYS)
        );

        refreshTokenRepository.save(entity);

        return rawToken;
    }

    private String hashToken(String token) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hash);
        } catch (Exception ex) {
            throw new IllegalStateException(
                    "Unable to hash refresh token", ex);
        }
    }

    private ResponseStatusException invalidCredentials() {
        return new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Invalid email or password");
    }

    private ResponseStatusException invalidRefreshToken() {
        return new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Invalid or expired refresh token");
    }
}
