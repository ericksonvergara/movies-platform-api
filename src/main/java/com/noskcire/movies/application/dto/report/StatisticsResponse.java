package com.noskcire.movies.application.dto.report;

import java.math.BigDecimal;

public record StatisticsResponse(
        BigDecimal totalIncome,
        Long totalRentals,
        Long totalReservations,
        Long totalLateFees,
        Long totalPersons,
        String mostRentedMovie,
        String topPerson
) {
}
