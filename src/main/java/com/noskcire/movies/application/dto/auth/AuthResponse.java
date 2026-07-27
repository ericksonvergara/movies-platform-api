package com.noskcire.movies.application.dto.auth;

public record AuthResponse (
        String accessToken,
        String refreshToken,
        String message
){}
