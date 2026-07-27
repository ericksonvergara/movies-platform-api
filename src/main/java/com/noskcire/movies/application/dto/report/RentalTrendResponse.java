package com.noskcire.movies.application.dto.report;

import java.math.BigDecimal;

public record RentalTrendResponse(
        Integer year,
        Integer month,
        Long totalRentals,
        BigDecimal totalIncome
) {
}
