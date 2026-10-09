package com.app.Q_Entertainment.Service;

import com.app.Q_Entertainment.Model.DTO.Request.AuthLoginRequest;
import com.app.Q_Entertainment.Model.DTO.Request.RefreshRequest;
import com.app.Q_Entertainment.Model.DTO.Request.RegisterDTO;
import com.app.Q_Entertainment.Model.DTO.Response.AuthLoginResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    void register(RegisterDTO registerDTO, HttpServletRequest request);
    AuthLoginResponse login(AuthLoginRequest authLoginRequest, HttpServletRequest request);

    Object refresh(RefreshRequest refreshRequest);
}
