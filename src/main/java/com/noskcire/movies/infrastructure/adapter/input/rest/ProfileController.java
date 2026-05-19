package com.noskcire.movies.infrastructure.adapter.input.rest;


import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @GetMapping
    public Map<String, Object> profile(
            Authentication authentication
    ) {
        return Map.of(
                "username",
                authentication.getName(),
                "roles",
                authentication.getAuthorities(),
                "authenticated",
                authentication.isAuthenticated()
        );
    }
}
