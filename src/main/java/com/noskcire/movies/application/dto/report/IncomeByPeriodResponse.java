package com.noskcire.movies.application.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;

public record IncomeByPeriodResponse(
        LocalDate startDate,
        LocalDate endDate,
        Long totalRentals,
        BigDecimal totalIncome,
        BigDecimal averageRentalAmount
) {
}
