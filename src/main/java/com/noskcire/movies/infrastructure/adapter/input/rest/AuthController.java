package com.noskcire.movies.infrastructure.adapter.input.rest;

import com.noskcire.movies.application.dto.auth.*;
import com.noskcire.movies.application.dto.response.ApiResponse;
import com.noskcire.movies.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Registrar un nuevo usuario", description = "Crea una cuenta de usuario con roles y credenciales de acceso")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario registrado correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de registro inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "El usuario ya existe")
    })
    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request){
        return authService.register(request);
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y devuelve un token JWT")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request){
        return authService.login(request);
    }

    @Operation(summary = "Refrescar token", description = "Renueva el token JWT utilizando un refresh token válido")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Token renovado correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Refresh token inválido o expirado")
    })
    @PostMapping("/refresh")
    public AuthResponse refreshToken(
            @RequestBody
            RefreshTokenRequest request
    ) {

        return authService.refreshToken(request);
    }

    @Operation(summary = "Cerrar sesión", description = "Invalida el token actual y cierra la sesión del usuario")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sesión cerrada correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        authService.logout();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Sesión cerrada correctamente.",
                        null,
                        LocalDateTime.now()
                )
        );
    }
}
