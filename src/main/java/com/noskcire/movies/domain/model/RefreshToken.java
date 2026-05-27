package com.noskcire.movies.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 1000)
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    private boolean revoked;
}
