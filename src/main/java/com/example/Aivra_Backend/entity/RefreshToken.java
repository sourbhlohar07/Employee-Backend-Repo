
package com.example.Aivra_Backend.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "admin_refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin_id", nullable = false)
    private AdminDetail admin;

    @Column(name = "token_hash", nullable = false,
            unique = true, length = 64)
    private String tokenHash;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected RefreshToken() {}

    public RefreshToken(AdminDetail admin, String tokenHash,
                        Instant expiresAt) {
        this.admin = admin;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public AdminDetail getAdmin() { return admin; }
    public String getTokenHash() { return tokenHash; }
    public Instant getExpiresAt() { return expiresAt; }
    public Instant getRevokedAt() { return revokedAt; }

    public boolean isExpired() {
        return !expiresAt.isAfter(Instant.now());
    }

    public boolean isRevoked() {
        return revokedAt != null;
    }

    public void revoke() {
        if (revokedAt == null) {
            revokedAt = Instant.now();
        }
    }
}
