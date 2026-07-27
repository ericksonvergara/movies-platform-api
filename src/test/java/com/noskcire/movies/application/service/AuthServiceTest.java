package com.noskcire.movies.application.service;

import com.noskcire.movies.application.dto.auth.AuthResponse;
import com.noskcire.movies.application.dto.auth.LoginRequest;
import com.noskcire.movies.application.dto.auth.RefreshTokenRequest;
import com.noskcire.movies.application.dto.auth.RegisterRequest;
import com.noskcire.movies.domain.enums.TokenType;
import com.noskcire.movies.domain.exception.BadRequestException;
import com.noskcire.movies.domain.exception.ResourceNotFoundException;
import com.noskcire.movies.domain.model.Person;
import com.noskcire.movies.domain.model.Role;
import com.noskcire.movies.domain.model.Token;
import com.noskcire.movies.domain.model.User;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.PersonRepository;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.RoleRepository;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.TokenRepository;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.UserRepository;
import com.noskcire.movies.infrastructure.security.CustomUserDetailsService;
import com.noskcire.movies.infrastructure.security.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private TokenRepository tokenRepository;

    private AuthService authService;

    @Captor
    private ArgumentCaptor<Person> personCaptor;
    @Captor
    private ArgumentCaptor<User> userCaptor;
    @Captor
    private ArgumentCaptor<Token> tokenCaptor;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private RefreshTokenRequest refreshTokenRequest;
    private Role clientRole;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        authService = new AuthService(
                userRepository, personRepository, roleRepository,
                passwordEncoder, authenticationManager, jwtService,
                customUserDetailsService, tokenRepository
        );

        registerRequest = new RegisterRequest(
                "Juan", "Perez", "12345678", "999888777",
                "juan@email.com", "Av. Siempre Viva 123",
                LocalDate.of(1990, 1, 1), "juanperez", "password123"
        );

        loginRequest = new LoginRequest("juanperez", "password123");
        refreshTokenRequest = new RefreshTokenRequest("refresh-token-value");

        clientRole = Role.builder().id(1L).name("CLIENT").build();

        userDetails = org.springframework.security.core.userdetails.User
                .builder()
                .username("juanperez")
                .password("encoded-pass")
                .authorities("ROLE_CLIENT")
                .build();
    }

    @Test
    void register_shouldCreateUserAndReturnAuthResponse() {
        when(userRepository.existsByUsername("juanperez")).thenReturn(false);
        when(roleRepository.findByName("CLIENT")).thenReturn(Optional.of(clientRole));
        when(passwordEncoder.encode("password123")).thenReturn("encoded-pass");
        when(customUserDetailsService.loadUserByUsername("juanperez")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("access-token-value");
        when(jwtService.generateRefreshToken(userDetails)).thenReturn("refresh-token-value");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("access-token-value", response.accessToken());
        assertEquals("refresh-token-value", response.refreshToken());
        assertEquals("Usuario creado correctamente.", response.message());

        verify(personRepository).save(personCaptor.capture());
        assertEquals("Juan", personCaptor.getValue().getNames());
        assertEquals("Perez", personCaptor.getValue().getLastNames());

        verify(userRepository).save(userCaptor.capture());
        assertEquals("juanperez", userCaptor.getValue().getUsername());
        assertEquals("encoded-pass", userCaptor.getValue().getPassword());
        assertTrue(userCaptor.getValue().isEnabled());
        assertEquals(clientRole, userCaptor.getValue().getRole());
        assertEquals(personCaptor.getValue(), userCaptor.getValue().getPerson());

        verify(tokenRepository, times(2)).save(tokenCaptor.capture());
        List<Token> savedTokens = tokenCaptor.getAllValues();
        assertEquals(2, savedTokens.size());
        assertEquals("access-token-value", savedTokens.get(0).getToken());
        assertEquals(TokenType.ACCESS, savedTokens.get(0).getType());
        assertEquals("refresh-token-value", savedTokens.get(1).getToken());
        assertEquals(TokenType.REFRESH, savedTokens.get(1).getType());
    }

    @Test
    void register_shouldThrowWhenUsernameAlreadyExists() {
        when(userRepository.existsByUsername("juanperez")).thenReturn(true);

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> authService.register(registerRequest)
        );
        assertEquals("El usuario ya existe.", ex.getMessage());
        verify(personRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldThrowWhenRoleNotFound() {
        when(userRepository.existsByUsername("juanperez")).thenReturn(false);
        when(roleRepository.findByName("CLIENT")).thenReturn(Optional.empty());

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> authService.register(registerRequest)
        );
        assertEquals("El rol no existe.", ex.getMessage());
        verify(personRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_shouldAuthenticateAndReturnAuthResponse() {
        User user = User.builder()
                .id(1L).username("juanperez").password("encoded-pass")
                .enabled(true).role(clientRole).build();

        when(userRepository.findByUsername("juanperez")).thenReturn(Optional.of(user));
        when(customUserDetailsService.loadUserByUsername("juanperez")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("new-access-token");
        when(jwtService.generateRefreshToken(userDetails)).thenReturn("new-refresh-token");

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("new-access-token", response.accessToken());
        assertEquals("new-refresh-token", response.refreshToken());
        assertEquals("Inicio de sesión exitoso.", response.message());

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("juanperez", "password123")
        );
        verify(tokenRepository, times(2)).save(any(Token.class));
    }

    @Test
    void login_shouldRevokeOldTokens() {
        User user = User.builder().id(1L).username("juanperez").build();
        Token oldToken = Token.builder().id(1L).revoked(false).build();
        List<Token> activeTokens = List.of(oldToken);

        when(userRepository.findByUsername("juanperez")).thenReturn(Optional.of(user));
        when(tokenRepository.findByUserAndRevokedFalse(user)).thenReturn(activeTokens);
        when(customUserDetailsService.loadUserByUsername("juanperez")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("token");
        when(jwtService.generateRefreshToken(userDetails)).thenReturn("refresh");

        authService.login(loginRequest);

        assertTrue(oldToken.isRevoked());
        verify(tokenRepository).saveAll(activeTokens);
    }

    @Test
    void login_shouldThrowWhenCredentialsAreInvalid() {
        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any());

        assertThrows(
                BadCredentialsException.class,
                () -> authService.login(loginRequest)
        );
    }

    @Test
    void refreshToken_shouldReturnNewAccessToken() {
        User user = User.builder().id(1L).username("juanperez").build();
        Token refreshToken = Token.builder()
                .id(1L).token("refresh-token-value")
                .type(TokenType.REFRESH).revoked(false)
                .user(user).build();

        when(tokenRepository.findByTokenAndRevokedFalse("refresh-token-value"))
                .thenReturn(Optional.of(refreshToken));
        when(customUserDetailsService.loadUserByUsername("juanperez")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("new-access-token");

        AuthResponse response = authService.refreshToken(refreshTokenRequest);

        assertNotNull(response);
        assertEquals("new-access-token", response.accessToken());
        assertEquals("refresh-token-value", response.refreshToken());
        assertEquals("Token renovado correctamente.", response.message());
    }

    @Test
    void refreshToken_shouldThrowWhenTokenNotFound() {
        when(tokenRepository.findByTokenAndRevokedFalse("refresh-token-value"))
                .thenReturn(Optional.empty());

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> authService.refreshToken(refreshTokenRequest)
        );
        assertEquals("Refresh token invalido.", ex.getMessage());
    }

    @Test
    void refreshToken_shouldThrowWhenTokenIsNotRefreshType() {
        Token accessToken = Token.builder()
                .id(1L).token("access-token")
                .type(TokenType.ACCESS).revoked(false)
                .build();

        when(tokenRepository.findByTokenAndRevokedFalse("refresh-token-value"))
                .thenReturn(Optional.of(accessToken));

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> authService.refreshToken(refreshTokenRequest)
        );
        assertEquals("Refresh token invalido.", ex.getMessage());
    }

    @Test
    void logout_shouldRevokeAllActiveTokens() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("juanperez");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User user = User.builder().id(1L).username("juanperez").build();
        Token token1 = Token.builder().id(1L).revoked(false).build();
        Token token2 = Token.builder().id(2L).revoked(false).build();
        List<Token> activeTokens = List.of(token1, token2);

        when(userRepository.findByUsername("juanperez")).thenReturn(Optional.of(user));
        when(tokenRepository.findByUserAndRevokedFalse(user)).thenReturn(activeTokens);

        authService.logout();

        assertTrue(token1.isRevoked());
        assertTrue(token2.isRevoked());
        verify(tokenRepository).saveAll(activeTokens);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void logout_shouldThrowWhenUserNotFound() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("unknown");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> authService.logout()
        );
    }
}
