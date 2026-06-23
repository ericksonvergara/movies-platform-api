package com.noskcire.movies.application.service;

import com.noskcire.movies.application.dto.auth.AuthResponse;
import com.noskcire.movies.application.dto.auth.LoginRequest;
import com.noskcire.movies.application.dto.auth.RefreshTokenRequest;
import com.noskcire.movies.application.dto.auth.RegisterRequest;
import com.noskcire.movies.domain.enums.PersonType;
import com.noskcire.movies.domain.enums.TokenType;
import com.noskcire.movies.domain.exception.BadRequestException;
import com.noskcire.movies.domain.exception.ResourceNotFoundException;
import com.noskcire.movies.domain.model.Person;
import com.noskcire.movies.domain.model.Token;
import com.noskcire.movies.domain.model.Role;
import com.noskcire.movies.domain.model.User;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.PersonRepository;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.TokenRepository;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.RoleRepository;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.UserRepository;
import com.noskcire.movies.infrastructure.security.CustomUserDetailsService;
import com.noskcire.movies.infrastructure.security.jwt.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    private final TokenRepository tokenRepository;


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
                .findByName("CLIENT")
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
                .type(
                    PersonType.CLIENT
                )
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

        List<Token> activeTokens =
                tokenRepository
                        .findByUserAndRevokedFalse(user);

        activeTokens.forEach(token ->
                token.setRevoked(true)
        );

        tokenRepository.saveAll(activeTokens);

        UserDetails userDetails =
                customUserDetailsService
                        .loadUserByUsername(
                                user.getUsername()
                        );

        String accessToken =
                jwtService.generateToken(userDetails);

        String refreshToken =
                jwtService.generateRefreshToken(userDetails);

        saveToken(
                user,
                accessToken,
                TokenType.ACCESS,
                LocalDateTime.now().plusHours(1)
        );

        saveToken(
                user,
                refreshToken,
                TokenType.REFRESH,
                LocalDateTime.now().plusDays(7)
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

        List<Token> activeTokens =
                tokenRepository
                        .findByUserAndRevokedFalse(user);

        activeTokens.forEach(token ->
                token.setRevoked(true)
        );

        tokenRepository.saveAll(activeTokens);


        UserDetails userDetails =
                customUserDetailsService
                        .loadUserByUsername(
                                loginRequest.username()
                        );

        String accessToken =
                jwtService.generateToken(userDetails);

        String refreshToken =
                jwtService.generateRefreshToken(userDetails);

        saveToken(
                user,
                accessToken,
                TokenType.ACCESS,
                LocalDateTime.now().plusHours(1)
        );

        saveToken(
                user,
                refreshToken,
                TokenType.REFRESH,
                LocalDateTime.now().plusDays(7)
        );

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Inicio de sesión exitoso."
        );
    }

    private void saveToken(
            User user,
            String jwt,
            TokenType type,
            LocalDateTime expiresAt
    ) {

        Token token =
                Token.builder()
                        .user(user)
                        .token(jwt)
                        .type(type)
                        .expiresAt(expiresAt)
                        .revoked(false)
                        .build();

        tokenRepository.save(token);
    }

    public AuthResponse refreshToken(
            RefreshTokenRequest refreshTokenRequest
    ){
        Token token =
                tokenRepository
                        .findByTokenAndRevokedFalse(
                                refreshTokenRequest.refreshToken()
                        )
                        .orElseThrow(
                                () -> new BadRequestException(
                                        "Refresh token invalido."
                                )
                        );

        if (
                token.getType()
                    != TokenType.REFRESH){
                throw new BadRequestException(
                        "Refresh token invalido."
                );
        }

        User user = token.getUser();

        UserDetails userDetails =
                customUserDetailsService
                        .loadUserByUsername(
                                user.getUsername()
                        );

        String newAccessToken =
                jwtService.generateToken(userDetails);

        return new AuthResponse(
                newAccessToken,
                token.getToken(),
                "Token renovado correctamente."
        );

    }

    public void logout(){

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String username = authentication.getName();

        User user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Usuario no encontrado."
                                )
                        );

        List<Token> tokens =
                tokenRepository
                        .findByUserAndRevokedFalse(user);

        tokens.forEach(token ->
                token.setRevoked(true)
        );

        tokenRepository.saveAll(tokens);
        SecurityContextHolder.clearContext();

    }

}
