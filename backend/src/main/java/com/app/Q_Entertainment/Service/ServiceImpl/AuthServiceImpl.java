package com.app.Q_Entertainment.Service.ServiceImpl;

import com.app.Q_Entertainment.Exception.EmailAlreadyExistsException;
import com.app.Q_Entertainment.Exception.InvalidUsernamePasswordException;
import com.app.Q_Entertainment.Exception.NotExistTokenException;
import com.app.Q_Entertainment.Model.DTO.Request.AuthLoginRequest;
import com.app.Q_Entertainment.Model.DTO.Request.RefreshRequest;
import com.app.Q_Entertainment.Model.DTO.Request.RegisterDTO;
import com.app.Q_Entertainment.Model.DTO.Response.AuthLoginResponse;
import com.app.Q_Entertainment.Model.Entity.RefreshTokens;
import com.app.Q_Entertainment.Model.Entity.User;
import com.app.Q_Entertainment.Repository.RefreshTokensRepository;
import com.app.Q_Entertainment.Repository.UsersRepository;
import com.app.Q_Entertainment.Security.CustomUserDetails;
import com.app.Q_Entertainment.Service.AuthService;
import com.app.Q_Entertainment.Util.DateUtil;
import com.app.Q_Entertainment.Util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenUtil jwtTokenUtil;

    private final CustomUserDetailsService customUserDetailsService;

    private final RefreshTokensRepository refreshTokensRepository;

    @Value("${jwt.expiration-rt}")
    private long validityInMillisecondsRT;

    @Override
    public void register(RegisterDTO registerDTO, HttpServletRequest request) {
        if (usersRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email " + registerDTO.getEmail() + " already exists");
        }
        User user = User.builder()
                .username(registerDTO.getUsername())
                .passwordHash(passwordEncoder.encode(registerDTO.getPassword()))
                .email(registerDTO.getEmail())
                .fullName(registerDTO.getFullName())
                .avatarUrl(registerDTO.getAvatarUrl())
                .isActive(true)
                .build();
        usersRepository.save(user);
    }

    @Override
    @Transactional
    public AuthLoginResponse login(AuthLoginRequest authLoginRequest, HttpServletRequest request) {
        User user = usersRepository.findByUsername(authLoginRequest.getUsername())
                .orElseThrow(() -> new InvalidUsernamePasswordException("Invalid username or password"));

        if (!passwordEncoder.matches(authLoginRequest.getPassword(), user.getPasswordHash())) {
            throw new InvalidUsernamePasswordException("Invalid username or password");
        }

        CustomUserDetails userDetails = customUserDetailsService.fromUser(user);

        // Revoke all old refresh tokens of this user before issuing a new one
        refreshTokensRepository.revokeAllByUserId(user.getId());

        Date expireRT = DateUtil.generateExpirationRefreshToken(validityInMillisecondsRT);

        // Step 1: Save a placeholder row to get the auto-generated DB id
        RefreshTokens savedToken = refreshTokensRepository.save(RefreshTokens.builder()
                .userId(user.getId())
                .refreshTokenHash("placeholder")          // will be updated in step 3
                .deviceInfo(request.getHeader("User-Agent"))
                .ipAddress(request.getRemoteAddr())
                .expiresAt(expireRT.toInstant())
                .isRevoked(false)
                .build());

        // Step 2: Generate refresh token JWT with the real DB id embedded
        String refreshTokenNew = jwtTokenUtil.generateRefreshToken(user, userDetails, savedToken.getId());

        // Step 3: Update the hash now that we have the actual token string
        refreshTokensRepository.updateHashById(savedToken.getId(), hashWithSHA256(refreshTokenNew));

        return AuthLoginResponse.builder()
                .accessToken(jwtTokenUtil.generateAccessToken(userDetails))
                .refreshToken(refreshTokenNew)
                .build();
    }

    @Override
    @Transactional
    public AuthLoginResponse refresh(RefreshRequest refreshRequest) {

        // 1. Verify JWT signature and parse claims (throws if tampered / expired)
        Claims claims = jwtTokenUtil.getClaims(refreshRequest.getRefreshToken());

        int userId = Integer.parseInt(claims.getSubject());
        long refreshTokenId = ((Number) claims.get("refreshTokenId")).longValue();
        String username = claims.get("username", String.class);

        // 2. Find the token record in DB
        RefreshTokens rt = refreshTokensRepository.findByUserIdAndRefreshTokenId(userId, refreshTokenId)
                .orElseThrow(() -> new NotExistTokenException("Refresh token not found in system"));

        // 3. RTR Security checks
        if (Boolean.TRUE.equals(rt.getIsRevoked())) {
            // Token reuse detected! Revoke ALL tokens of this user (possible token theft)
            refreshTokensRepository.revokeAllByUserId(userId);
            throw new NotExistTokenException("Refresh token has been revoked — possible token theft detected");
        }

        if (rt.getExpiresAt().isBefore(java.time.Instant.now())) {
            throw new NotExistTokenException("Refresh token has expired");
        }

        // 4. Verify token hash matches what's stored in DB
        String incomingHash = hashWithSHA256(refreshRequest.getRefreshToken());
        if (!incomingHash.equals(rt.getRefreshTokenHash())) {
            throw new NotExistTokenException("Refresh token hash mismatch");
        }

        // 5. RTR: Revoke the old token immediately
        refreshTokensRepository.revokeById(rt.getId());

        // 6. Load user and issue a brand-new token pair
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new InvalidUsernamePasswordException("User not found"));
        CustomUserDetails userDetails = customUserDetailsService.fromUser(user);

        Date expireRT = DateUtil.generateExpirationRefreshToken(validityInMillisecondsRT);

        // Save placeholder → get DB id → update hash (same flow as login)
        RefreshTokens newToken = refreshTokensRepository.save(RefreshTokens.builder()
                .userId(userId)
                .refreshTokenHash("placeholder")
                .deviceInfo(rt.getDeviceInfo())
                .ipAddress(rt.getIpAddress())
                .expiresAt(expireRT.toInstant())
                .isRevoked(false)
                .build());

        String newRefreshToken = jwtTokenUtil.generateRefreshToken(user, userDetails, newToken.getId());
        refreshTokensRepository.updateHashById(newToken.getId(), hashWithSHA256(newRefreshToken));

        return AuthLoginResponse.builder()
                .accessToken(jwtTokenUtil.generateAccessToken(userDetails))
                .refreshToken(newRefreshToken)
                .build();
    }

    /**
     * Hash a token string using SHA-256.
     * BCrypt is NOT suitable for JWT tokens because BCrypt has a 72-byte input limit.
     * JWT tokens are high-entropy strings, so SHA-256 is safe and correct here.
     */
    private String hashWithSHA256(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(token.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
