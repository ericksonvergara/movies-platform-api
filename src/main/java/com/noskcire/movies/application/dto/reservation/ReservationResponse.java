package com.noskcire.movies.application.dto.reservation;

import com.noskcire.movies.domain.enums.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        Long movieId,
        String movieTitle,
        Long clientId,
        String clientName,
        LocalDateTime reservationDate,
        LocalDateTime expirationDate,
        ReservationStatus status,
        boolean notificationSent,
        LocalDateTime fulfilledAt
) {
}
