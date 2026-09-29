package com.app.Q_Entertainment.Controller;

import com.app.Q_Entertainment.Model.DTO.ApiResponse;
import com.app.Q_Entertainment.Model.DTO.Request.RegisterDTO;
import com.app.Q_Entertainment.Model.DTO.ResponseUtil;
import com.app.Q_Entertainment.Service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<?> register(RegisterDTO registerDTO, HttpServletRequest request){
        authService.register(registerDTO, request);
        return ResponseUtil.success(null, "Register Successfully", request.getRequestURI());
    }

}
