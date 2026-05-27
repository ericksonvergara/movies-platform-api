package com.noskcire.movies.application.dto.movie;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateMovieRequest(
        @NotBlank(message = "El título debe ser obligatorio")
        String title,

        @NotBlank(message = "La descripción es obligatoria")
        String description,

        @NotBlank(message = "El genero es obligatorio")
        String gender,

        @NotNull(message = "El año es obligatio.")
        Integer releaseYear,

        Integer stock,

        @NotNull(message = "El precio debe ser obligatorio")
        @Positive(message = "El precio debe ser mayor a $0")
        BigDecimal rentalPrice
) {
}
