package com.noskcire.movies.application.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateUserRequest(

        @NotBlank(message = "Los nombres son obligatorios.")
        String names,

        @NotBlank(message = "Los apellidos son obligatorios.")
        String lastNames,

        @NotBlank(message = "El documento es obligatorio.")
        String document,

        @NotBlank(message = "El teléfono es obligatorio.")
        String phone,

        @Email(message = "El correo no es válido.")
        @NotBlank(message = "El correo es obligatorio.")
        String email,

        @NotBlank(message = "La dirección es obligatoria.")
        String address,

        @Past(message = "La fecha de nacimiento debe ser válida.")
        LocalDate dateBirth,

        @NotBlank(message = "El username es obligatorio.")
        @Size(min = 4, max = 20,
                message = "El username debe tener entre 4 y 20 caracteres.")
        String username,

        @NotBlank(message = "La contraseña es obligatoria.")
        @Size(min = 6,
                message = "La contraseña debe tener mínimo 6 caracteres.")
        String password,

        @NotBlank(message = "El rol es obligatorio.")
        String role



) {
}
