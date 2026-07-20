package com.noskcire.movies.application.dto.report;

public record ReservationRankingResponse(

        Long clientId,
        String clientName,
        Long totalReservations,
        Long activeReservations,
        Long notifiedReservations,
        Long fulfilledReservations,
        Long cancelledReservations,
        Long expiredReservations
) {
}
