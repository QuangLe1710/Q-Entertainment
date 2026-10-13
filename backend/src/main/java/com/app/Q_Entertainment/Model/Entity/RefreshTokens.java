
package com.app.Q_Entertainment.Model.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;

@Entity
@Builder
@Table(name = "refresh_tokens")
@SQLRestriction("is_revoked IS FALSE")
public class RefreshTokens extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int userId;

    private String refreshTokenHash;

    private String deviceInfo;

    private String ipAddress;

    private Instant expiresAt;

    @Column(name = "is_revoked")
    private Boolean isRevoked;

}
