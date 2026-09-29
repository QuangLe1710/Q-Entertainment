package com.app.Q_Entertainment.Service;

import com.app.Q_Entertainment.Model.DTO.Request.RegisterDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    void register(RegisterDTO registerDTO, HttpServletRequest request);
}
