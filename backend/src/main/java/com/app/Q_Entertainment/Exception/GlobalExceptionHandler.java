package com.app.Q_Entertainment.Exception;

import com.app.Q_Entertainment.Model.DTO.ApiResponse;
import com.app.Q_Entertainment.Model.DTO.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ApiResponse<?> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception, HttpServletRequest request){
        return ResponseUtil.error(
                Arrays.asList(exception.getMessage()),
                "Please enter an another email",
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI()
        );
    }

}
