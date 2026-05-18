package com.noskcire.movies.application.dto.auth;

import java.time.LocalDate;

public record RegisterRequest (
        String names,

        String lastNames,

        String document,

        String phone,

        String email,

        String address,

        LocalDate dateBirth,

        String username,

        String password,

        String role
){}
