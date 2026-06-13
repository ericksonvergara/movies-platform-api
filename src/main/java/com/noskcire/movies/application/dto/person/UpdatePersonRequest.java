package com.noskcire.movies.application.dto.person;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record UpdatePersonRequest(

        String names,

        String lastNames,

        String document,

        String phone,

        @Email(message = "El correo no es válido.")
        String email,

        String address,

        @Past(message = "La fecha de nacimiento debe ser válida.")
        LocalDate dateBirth
) {
}
