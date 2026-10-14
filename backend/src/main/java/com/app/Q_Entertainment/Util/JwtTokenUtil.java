package com.app.Q_Entertainment.Util;

import com.app.Q_Entertainment.Model.Entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private final SecretKey secretKey;
    private final long validityInMilliseconds;
    private final long validityInMillisecondsRT;

    public JwtTokenUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long validityInMilliseconds,
            @Value("${jwt.expiration-rt}") long validityInMillisecondsRT
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.validityInMilliseconds = validityInMilliseconds;
        this.validityInMillisecondsRT = validityInMillisecondsRT;
    }

    public String generateAccessToken(UserDetails userDetails) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        System.out.println("1 : generateAccessToken");

        // In jjwt 0.12.x, Claims from .build() are immutable — use .subject() and .claim()
        // on the JwtBuilder directly instead of building a Claims object and mutating it.
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("ROLES", userDetails.getAuthorities())
                .claim("token_type", "ACCESS")
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Generate a refresh token JWT.
     * @param user       the user entity
     * @param userDetails Spring Security user details
     * @param dbTokenId  the auto-generated DB id of the saved RefreshTokens row,
     *                   embedded as claim so the /refresh endpoint can look it up
     */
    public String generateRefreshToken(User user, UserDetails userDetails, long dbTokenId) {
        Date now = new Date();
        Date expireRT = DateUtil.generateExpirationRefreshToken(validityInMillisecondsRT);

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))   // subject = userId (string)
                .claim("username", userDetails.getUsername())
                .claim("refreshTokenId", dbTokenId)     // DB row id for fast lookup
                .claim("token_type", "REFRESH")
                .issuedAt(now)
                .expiration(expireRT)
                .signWith(secretKey)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getRefreshTokenId(String token){
        return getClaims(token).get("refreshTokenId").toString();
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    public Authentication getAuthentication(String token, UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
