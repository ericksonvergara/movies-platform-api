package com.noskcire.movies.application.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(

        @NotBlank
        String role,

        boolean enabled
) {
}
