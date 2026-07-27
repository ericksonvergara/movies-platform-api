package com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection;

import java.math.BigDecimal;

public record RentalTrendProjection(
        Integer year,
        Integer month,
        Long totalRentals,
        BigDecimal totalIncome
) {
}
