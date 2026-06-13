package com.noskcire.movies.application.dto.person;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record CreatePersonRequest(

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
        LocalDate dateBirth
) {
}
