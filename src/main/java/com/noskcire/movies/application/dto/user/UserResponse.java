package com.noskcire.movies.application.dto.user;

public record UserResponse(
        Long id,
        String username,
        String role,
        boolean enabled
) {
}
