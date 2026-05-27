package com.noskcire.movies.application.service;

import com.noskcire.movies.domain.model.RefreshToken;
import com.noskcire.movies.domain.model.User;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${application.security.jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    public RefreshToken createRefreshToken(
            User user,
            String token
    ) {

        refreshTokenRepository
                .findByUser(user)
                .ifPresent(
                        refreshTokenRepository::delete
                );

        RefreshToken refreshToken =
                RefreshToken.builder()
                        .token(token)
                        .user(user)
                        .expiresAt(
                                LocalDateTime.now()
                                        .plusSeconds(
                                                refreshTokenExpiration / 1000
                                        )
                        )
                        .revoked(false)
                        .build();

        return  refreshTokenRepository.save(refreshToken);
    }
}
