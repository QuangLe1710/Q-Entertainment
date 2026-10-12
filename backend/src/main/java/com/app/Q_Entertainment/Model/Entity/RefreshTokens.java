
package com.app.Q_Entertainment.Model.Entity;

import jakarta.persistence.*;
import lombok.Builder;

import java.time.Instant;

@Entity
@Builder
@Table(name = "refresh_tokens")
public class RefreshTokens extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int userId;

    private String refreshTokenHash;

    private String deviceInfo;

    private String ipAddress;

    private Instant expiresAt;

    private Boolean isRevoked;

}
