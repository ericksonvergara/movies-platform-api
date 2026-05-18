package com.noskcire.movies.application.dto.auth;

import java.time.LocalDate;

public record AuthResponse (
        String token,
        String message
){}
