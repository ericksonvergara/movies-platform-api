package com.noskcire.movies.application.dto.auth;

import java.time.LocalDate;

public record AuthResponse (
        String accessToken,
        String refreshToken,
        String message
){}
