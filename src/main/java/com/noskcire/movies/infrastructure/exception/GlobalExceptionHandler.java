package com.noskcire.movies.infrastructure.exception;

import com.noskcire.movies.application.dto.response.ErrorResponse;
import com.noskcire.movies.domain.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)

    @ResponseStatus(HttpStatus.BAD_REQUEST)

    public ErrorResponse handleBadRequest(
            BadRequestException ex
    ) {
        return new ErrorResponse(
                400,
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)

    @ResponseStatus(HttpStatus.UNAUTHORIZED)

    public ErrorResponse handleBadCredentials(
            BadCredentialsException ex
    ) {
        return new ErrorResponse(
                401,
                "Usuario o contraseña incorrectos.",
                LocalDateTime.now()

        );
    }
}
