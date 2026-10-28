
package com.example.Aivra_Backend.service.impl;

import com.example.Aivra_Backend.entity.AdminDetail;
import com.example.Aivra_Backend.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class JwtServiceImpl implements JwtService {

    private final JwtEncoder jwtEncoder;
    private final String issuer;
    private final long accessTokenMinutes;

    public JwtServiceImpl(
            JwtEncoder jwtEncoder,
            @Value("${app.jwt.issuer}") String issuer,
            @Value("${app.jwt.access-minutes:50}")
            long accessTokenMinutes) {

        this.jwtEncoder = jwtEncoder;
        this.issuer = issuer;
        this.accessTokenMinutes = accessTokenMinutes;
    }

    @Override
    public String generateAccessToken(AdminDetail admin) {

        Instant now = Instant.now();

        Instant expiresAt = now.plus(
                accessTokenMinutes,
                ChronoUnit.MINUTES
        );

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(admin.getId().toString())
                .claim("email", admin.getEmail())
                .claim("roles", List.of(admin.getRole()))
                .build();

        JwsHeader header = JwsHeader
                .with(MacAlgorithm.HS256)
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims)
        ).getTokenValue();
    }

    @Override
    public long getAccessTokenLifetimeSeconds() {
        return accessTokenMinutes * 60;
    }
}
