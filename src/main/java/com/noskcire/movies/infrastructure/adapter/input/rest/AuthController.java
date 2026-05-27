package com.noskcire.movies.infrastructure.adapter.input.rest;

import com.noskcire.movies.application.dto.auth.AuthResponse;
import com.noskcire.movies.application.dto.auth.LoginRequest;
import com.noskcire.movies.application.dto.auth.RefreshTokenRequest;
import com.noskcire.movies.application.dto.auth.RegisterRequest;
import com.noskcire.movies.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request){
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request){
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public AuthResponse refreshToken(
            @RequestBody
            RefreshTokenRequest request
    ) {

        return authService.refreshToken(request);
    }
}
