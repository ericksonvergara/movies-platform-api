package com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection;

import java.math.BigDecimal;

public record StatisticsProjection(
        BigDecimal totalIncome,
        Long totalRentals,
        Long totalReservations,
        Long totalLateFees,
        Long totalPersons,
        String mostRentedMovie,
        String topPerson
) {
}
