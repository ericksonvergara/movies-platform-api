package com.noskcire.movies.application.dto.rental;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CreateRentalRequest(
        @NotNull(message = "El cliente es obligatorio.")
        Long clientId,

        @NotNull(message = "La fecha de devolución")
        @Future(message = "LLa frcha de devolución debe ser futura")
        LocalDate expectedReturnDate,

        @NotEmpty(message = "Debe seleccionar al menos una pelicula.")
        List<@Valid CreateRentalDetailRequest> movies
) {
}
