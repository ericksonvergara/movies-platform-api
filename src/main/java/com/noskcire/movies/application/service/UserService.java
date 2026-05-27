package com.noskcire.movies.application.service;

import com.noskcire.movies.application.dto.user.CreateUserRequest;
import com.noskcire.movies.application.dto.user.UpdateUserRequest;
import com.noskcire.movies.application.dto.user.UserResponse;
import com.noskcire.movies.domain.exception.BadRequestException;
import com.noskcire.movies.domain.exception.ResourceNotFoundException;
import com.noskcire.movies.domain.model.Person;
import com.noskcire.movies.domain.model.Role;
import com.noskcire.movies.domain.model.User;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.PersonRepository;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.RoleRepository;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;

    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole().getName(),
                user.isEnabled()
        );
    }

    public void deactivateUser(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Usuario no encontrado."
                        )
                );

        user.setEnabled(false);
        userRepository.save(user);
    }

    public UserResponse getUserById(
            Long id
    ) {
        User user =
                userRepository.findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Usuario no encontrado."
                                )
                        );

        return mapToResponse(user);
    }

    public UserResponse CreateUser(
            CreateUserRequest createUserRequest
    ) {
        if (
                userRepository.existsByUsername(
                        createUserRequest.username()
                )
        ) {
            throw new BadRequestException("El usuario ya existe.");
        }

        Role role = roleRepository
                .findByName(createUserRequest.role())
                .orElseThrow(
                        () -> new BadRequestException(
                                "El rol no existe."
                        )
                );

        Person person =
                Person.builder()
                        .names(createUserRequest.names())
                        .lastNames(createUserRequest.lastNames())
                        .document(createUserRequest.document())
                        .phone(createUserRequest.phone())
                        .email(createUserRequest.email())
                        .address(createUserRequest.address())
                        .dateBirth(createUserRequest.dateBirth())
                        .build();
        personRepository.save(person);

        User user = User.builder()
                .username(createUserRequest.username())
                .password(
                        passwordEncoder.encode(
                                createUserRequest.password()
                        )
                )
                .role(role)
                .enabled(true)
                .build();

        userRepository.save(user);
        return mapToResponse(user);
    }

    public UserResponse updateUser(
            Long id,
            UpdateUserRequest updateUserRequest
    ) {
        User user =
                userRepository.findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Usuario no encontrado."

                                )
                        );
        Role role =
                roleRepository.findByName(
                        updateUserRequest.role()
                )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Rol no encontrado."
                                )
                        );

        user.setRole(role);
        user.setEnabled(updateUserRequest.enabled());
        userRepository.save(user);
        return mapToResponse(user);
    }
}
