package com.noskcire.movies.application.service;

import com.noskcire.movies.application.dto.auth.AuthResponse;
import com.noskcire.movies.application.dto.auth.LoginRequest;
import com.noskcire.movies.application.dto.auth.RegisterRequest;
import com.noskcire.movies.domain.exception.BadRequestException;
import com.noskcire.movies.domain.model.Person;
import com.noskcire.movies.domain.model.Role;
import com.noskcire.movies.domain.model.User;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.PersonRepository;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.RoleRepository;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

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
                .build();
        userRepository.save(user);

        return new AuthResponse(null,"Usuario creado");
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

        return new AuthResponse(
                null,
                "Inicio de sesión exitoso."
        );
    }

}
