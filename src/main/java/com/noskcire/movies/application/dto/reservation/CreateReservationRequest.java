package com.noskcire.movies.application.dto.reservation;

import jakarta.validation.constraints.NotNull;

public record CreateReservationRequest(
        Long clientId,

        @NotNull
        Long movieId
) {
}
