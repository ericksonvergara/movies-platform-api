package com.noskcire.movies.application.dto.auth;

public record LogoutRequest(
        String refreshToken
) {
}
