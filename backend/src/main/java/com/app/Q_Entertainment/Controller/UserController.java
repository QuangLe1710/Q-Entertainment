package com.app.Q_Entertainment.Controller;

import com.app.Q_Entertainment.Model.DTO.ApiResponse;
import com.app.Q_Entertainment.Model.DTO.ResponseUtil;
import com.app.Q_Entertainment.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("private/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/listUser")
    public ApiResponse<?> getListUser(HttpServletRequest request){
        return ResponseUtil.success(userService.getListUser(), "Get list user is successfully", request.getRequestURI());
    }

}
