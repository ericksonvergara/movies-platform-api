package com.noskcire.movies.infrastructure.security;


import com.noskcire.movies.domain.model.User;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repository;

    public UserDetails loadUserByUsername (
            String username
    ) throws UsernameNotFoundException {
        User user = repository.findByUsername(username).orElseThrow(
                ()->new UsernameNotFoundException(
                        "Usuario no encontrado"
                )
        );

        return org.springframework.security.core.userdetails
                .User
                .builder()
                .username(
                        user.getUsername()
                )

                .password(
                        user.getPassword()
                )

                .roles(
                        user.getRole().getName()
                )

                .build();


    }
}
