package com.noskcire.movies.infrastructure.adapter.output.persistence.repository;

import com.noskcire.movies.domain.model.RefreshToken;
import com.noskcire.movies.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByTokenAndRevokedFalse(String token);

    Optional<RefreshToken> findByUser(User user);
}
