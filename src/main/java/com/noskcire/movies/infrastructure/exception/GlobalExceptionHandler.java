package com.noskcire.movies.infrastructure.exception;

import com.noskcire.movies.application.dto.response.ErrorResponse;
import com.noskcire.movies.domain.exception.BadRequestException;
import com.noskcire.movies.domain.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
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
                false,
                ex.getMessage(),
                null,
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleBadCredentials(
            BadCredentialsException ex
    ) {
        return new ErrorResponse(
                false,
                "Usuario o contraseña incorrectos.",
                null,
                LocalDateTime.now()

        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex
    ) {
        ErrorResponse error =
                new ErrorResponse(
                        false,
                        ex.getMessage(),
                        null,
                        LocalDateTime.now()
                );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex
    ) {
        String message = ex.getBindingResult()
                .getFieldError()
                .getDefaultMessage();

        ErrorResponse error = new ErrorResponse(
                false,
                message,
                null,
                LocalDateTime.now()
        );

        return ResponseEntity
                .badRequest()
                .body(error);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AuthorizationDeniedException ex
    ) {
        ErrorResponse error =
                new ErrorResponse(
                        false,
                        "No tienes permisos para acceder a este recurso.",
                        null,
                        LocalDateTime.now()
                );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(
            Exception ex
    ) {
        ErrorResponse error =
                new ErrorResponse(
                        false,
                        "Ocurrio un error interno",
                        null,
                        LocalDateTime.now()
                );


        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    @ExceptionHandler(DisabledException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleDisabledException(
            DisabledException ex
    ) {
        return new ErrorResponse(
                false,
                "El usuario está deshabilitado.",
                null,
                LocalDateTime.now()
        );
    }
}
