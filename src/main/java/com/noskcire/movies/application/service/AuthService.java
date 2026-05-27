package com.noskcire.movies.application.service;

import com.noskcire.movies.application.dto.auth.AuthResponse;
import com.noskcire.movies.application.dto.auth.LoginRequest;
import com.noskcire.movies.application.dto.auth.RefreshTokenRequest;
import com.noskcire.movies.application.dto.auth.RegisterRequest;
import com.noskcire.movies.domain.exception.BadRequestException;
import com.noskcire.movies.domain.model.Person;
import com.noskcire.movies.domain.model.RefreshToken;
import com.noskcire.movies.domain.model.Role;
import com.noskcire.movies.domain.model.User;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.PersonRepository;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.RefreshTokenRepository;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.RoleRepository;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.UserRepository;
import com.noskcire.movies.infrastructure.security.CustomUserDetailsService;
import com.noskcire.movies.infrastructure.security.jwt.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;


    @Transactional
    public AuthResponse register(
            RegisterRequest registerRequest
    ) {
        if (
                userRepository.existsByUsername(registerRequest.username()
                )
        ){
            throw  new BadRequestException("El usuario ya existe.");
        }
        Role role = roleRepository
                .findByName(registerRequest.role())
                .orElseThrow(
                        () -> new BadRequestException(
                                "El rol no existe."
                        )
                );

        Person person = Person.builder()
                .names(registerRequest.names())
                .lastNames(registerRequest.lastNames())
                .document(registerRequest.document())
                .phone(registerRequest.phone())
                .email(registerRequest.email())
                .address(registerRequest.address())
                .dateBirth(registerRequest.dateBirth())
                .build();
        personRepository.save(person);

        User user = User.builder()
                .username(registerRequest.username())
                .password(passwordEncoder.encode(registerRequest.password()))
                .enabled(true)
                .person(person)
                .role(role)
                .build();
        userRepository.save(user);

        UserDetails userDetails =
                customUserDetailsService
                        .loadUserByUsername(
                                user.getUsername()
                        );

        String accessToken =
                jwtService.generateToken(userDetails);

        String refreshToken =
                jwtService.generateRefreshToken(userDetails);

        refreshTokenService.createRefreshToken(
                user,
                refreshToken
        );

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Usuario creado correctamente.");
    }

    public AuthResponse login(
            LoginRequest loginRequest
    ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        User user =
                userRepository.findByUsername(
                        loginRequest.username()
                ).orElseThrow();


        UserDetails userDetails =
                customUserDetailsService
                        .loadUserByUsername(
                                loginRequest.username()
                        );

        String accessToken =
                jwtService.generateToken(userDetails);

        String refreshToken =
                jwtService.generateRefreshToken(userDetails);

        refreshTokenService.createRefreshToken(
                user,
                refreshToken
        );

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Inicio de sesión exitoso."
        );
    }

    public AuthResponse refreshToken(
            RefreshTokenRequest refreshTokenRequest
    ){
        RefreshToken refreshToken =
                refreshTokenRepository
                        .findByTokenAndRevokedFalse(
                                refreshTokenRequest.refreshToken()
                        )
                        .orElseThrow(
                                () -> new BadRequestException(
                                        "Refresh token invalido."
                                )
                        );

        if (
                refreshToken.getExpiresAt()
                        .isBefore(
                                LocalDateTime.now()
                        )
        ) {
            throw new BadRequestException(
                    "Refresh token expirado."
            );
        }

        User user = refreshToken.getUser();

        UserDetails userDetails =
                customUserDetailsService
                        .loadUserByUsername(
                                user.getUsername()
                        );

        String newAccessToken =
                jwtService.generateToken(userDetails);

        return new AuthResponse(
                newAccessToken,
                refreshToken.getToken(),
                "Token renovado correctamente."
        );

    }

}
