package com.app.Q_Entertainment.Service.ServiceImpl;

import com.app.Q_Entertainment.Exception.EmailAlreadyExistsException;
import com.app.Q_Entertainment.Model.DTO.Request.AuthLoginRequest;
import com.app.Q_Entertainment.Model.DTO.Request.RegisterDTO;
import com.app.Q_Entertainment.Model.Entity.User;
import com.app.Q_Entertainment.Repository.UserRepository;
import com.app.Q_Entertainment.Service.AuthService;
import com.app.Q_Entertainment.Util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserDetailsService userDetailsService;

    @Override
    public void register(RegisterDTO registerDTO, HttpServletRequest request) {
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email " + registerDTO.getEmail() + " already exists");
        }
        User user = User.builder()
                .username(registerDTO.getUsername())
                .passwordHash(passwordEncoder.encode(registerDTO.getPassword()))
                .email(registerDTO.getEmail())
                .fullName(registerDTO.getFullName())
                .avatarUrl(registerDTO.getAvatarUrl())
                .build();
        userRepository.save(user);
    }

    @Override
    public String login(AuthLoginRequest authLoginRequest, HttpServletRequest request) {
        User user = userRepository.findByUsername(authLoginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!passwordEncoder.matches(authLoginRequest.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        return jwtTokenUtil.generateToken(userDetails);
    }
}
