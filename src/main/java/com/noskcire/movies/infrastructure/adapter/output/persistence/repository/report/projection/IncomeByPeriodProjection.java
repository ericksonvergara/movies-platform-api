package com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public record IncomeByPeriodProjection(
        LocalDate startDate,
        LocalDate endDate,
        Long totalRentals,
        BigDecimal totalIncome,
        BigDecimal averageRentalAmount
) {
}
