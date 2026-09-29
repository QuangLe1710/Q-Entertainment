package com.app.Q_Entertainment.Service.ServiceImpl;

import com.app.Q_Entertainment.Exception.EmailAlreadyExistsException;
import com.app.Q_Entertainment.Model.DTO.Request.RegisterDTO;
import com.app.Q_Entertainment.Model.Entity.User;
import com.app.Q_Entertainment.Repository.UserRepository;
import com.app.Q_Entertainment.Service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

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
}
