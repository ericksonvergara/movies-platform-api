package com.noskcire.movies.application.dto.rental;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateRentalDetailRequest(
        @NotNull(message = "La pelicula es obligatoria.")
        Long movieId,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser mayor a 0.")
        Integer quantity
) {
}
