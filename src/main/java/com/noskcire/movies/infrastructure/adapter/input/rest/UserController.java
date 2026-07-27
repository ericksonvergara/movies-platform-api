package com.noskcire.movies.infrastructure.adapter.input.rest;

import com.noskcire.movies.application.dto.user.CreateUserRequest;
import com.noskcire.movies.application.dto.user.UpdateUserRequest;
import com.noskcire.movies.application.dto.user.UserResponse;
import com.noskcire.movies.application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve el listado completo de usuarios del sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de usuarios obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Desactivar usuario", description = "Desactiva la cuenta de un usuario por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario desactivado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateUser(
            @PathVariable Long id
    ) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener usuario por ID", description = "Devuelve los datos de un usuario específico por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }

    @Operation(summary = "Crear un nuevo usuario", description = "Registra un nuevo usuario en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos del usuario inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<UserResponse> createUser(
        @Valid
        @RequestBody
        CreateUserRequest createUserRequest
    ) {
        UserResponse userResponse = userService.CreateUser(createUserRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userResponse);
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid
            @RequestBody
            UpdateUserRequest updateUserRequest
    ) {
        UserResponse userResponse =
                userService.updateUser(
                        id,
                        updateUserRequest
                );

        return ResponseEntity.ok(userResponse);
    }
}
