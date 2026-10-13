package com.app.Q_Entertainment.Service.ServiceImpl;

import com.app.Q_Entertainment.Exception.EmailAlreadyExistsException;
import com.app.Q_Entertainment.Exception.InvalidUsernamePasswordException;
import com.app.Q_Entertainment.Model.DTO.Request.AuthLoginRequest;
import com.app.Q_Entertainment.Model.DTO.Request.RefreshRequest;
import com.app.Q_Entertainment.Model.DTO.Request.RegisterDTO;
import com.app.Q_Entertainment.Model.DTO.Response.AuthLoginResponse;
import com.app.Q_Entertainment.Model.Entity.RefreshTokens;
import com.app.Q_Entertainment.Model.Entity.User;
import com.app.Q_Entertainment.Repository.RefreshTokensRepository;
import com.app.Q_Entertainment.Repository.UsersRepository;
import com.app.Q_Entertainment.Service.AuthService;
import com.app.Q_Entertainment.Util.DateUtil;
import com.app.Q_Entertainment.Util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Date;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenUtil jwtTokenUtil;

    private final CustomUserDetailsService customUserDetailsService;

    private final RefreshTokensRepository refreshTokensRepository;

    @Value("${jwt.expiration-rt}")
    private final long validityInMillisecondsRT;

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

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
        System.out.println("2 : login service impl");

        // Revoke all old RefreshToken of userId before save new
        refreshTokensRepository.revokeAllByUserId(user.getId());

        // Login and generate new refresh token and save it into database
        String refreshTokenNew = jwtTokenUtil.generateRefreshToken(user, userDetails);
        Date expireRT = DateUtil.generateExpirationRefreshToken(validityInMillisecondsRT);
        refreshTokensRepository.save(RefreshTokens.builder()
                        .userId(user.getId())
                        .refreshTokenHash(passwordEncoder.encode(refreshTokenNew))
                        .deviceInfo(request.getHeader("User-Agent"))
                        .ipAddress(request.getRemoteAddr())
                        .expiresAt(expireRT.toInstant())
                .build());

        return AuthLoginResponse.builder()
                .accessToken(jwtTokenUtil.generateAccessToken(userDetails))
                .refreshToken(refreshTokenNew)
                .build();
    }

    @Override
    public AuthLoginResponse refresh(RefreshRequest refreshRequest) {

        Claims claims = jwtTokenUtil.getClaims(refreshRequest.getRefreshToken());

        if( jwtTokenUtil.isTokenExpired(refreshRequest.getRefreshToken()) ) {

        }

        return AuthLoginResponse.builder()
                .accessToken(jwtTokenUtil.generateAccessToken(userDetails))
                .refreshToken(jwtTokenUtil.generateRefreshToken(user, userDetails))
                .build();
    }
}
