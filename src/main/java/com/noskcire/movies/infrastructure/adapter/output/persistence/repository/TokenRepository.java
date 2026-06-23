package com.noskcire.movies.infrastructure.adapter.output.persistence.repository;

import com.noskcire.movies.domain.model.Token;
import com.noskcire.movies.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    Optional<Token> findByTokenAndRevokedFalse(String token);

    Optional<Token> findByUser(User user);

    List<Token> findByUserAndRevokedFalse(
            User user
    );
}
