package com.noskcire.movies.infrastructure.adapter.input.rest;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Operation(summary = "Obtener perfil del usuario autenticado", description = "Devuelve la información del usuario actualmente autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @GetMapping
    public Map<String, Object> profile(
            Authentication authentication
    ) {
        return Map.of(
                "username",
                authentication.getName(),
                "roles",
                authentication.getAuthorities(),
                "authenticated",
                authentication.isAuthenticated()
        );
    }
}
