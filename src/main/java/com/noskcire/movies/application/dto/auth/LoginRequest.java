package com.noskcire.movies.application.dto.auth;

public record LoginRequest (
        String username,
        String password
) {}
