package com.noskcire.movies.infrastructure.adapter.input.rest;

import com.noskcire.movies.application.dto.user.CreateUserRequest;
import com.noskcire.movies.application.dto.user.UpdateUserRequest;
import com.noskcire.movies.application.dto.user.UserResponse;
import com.noskcire.movies.application.service.UserService;
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

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateUser(
            @PathVariable Long id
    ) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }

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
