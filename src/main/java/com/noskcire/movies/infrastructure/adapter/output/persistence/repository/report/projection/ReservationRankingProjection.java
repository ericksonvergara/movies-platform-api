package com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection;

public record ReservationRankingProjection(
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
