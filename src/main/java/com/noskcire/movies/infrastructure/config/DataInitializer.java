package com.noskcire.movies.infrastructure.config;

import com.noskcire.movies.domain.model.Role;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("CLIENT");
        createRoleIfNotExists("EMPLOYEE");
        createRoleIfNotExists("MANAGER");
    }

    private void createRoleIfNotExists(
            String roleName
    ) {
        roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role role = Role.builder()
                            .name(roleName)
                            .build();

                    return roleRepository.save(role);
                });
    }
}
