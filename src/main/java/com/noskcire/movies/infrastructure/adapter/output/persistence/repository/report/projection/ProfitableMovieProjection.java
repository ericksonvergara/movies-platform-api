package com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection;

import java.math.BigDecimal;

public record ProfitableMovieProjection(
        Long movieId,
        String title,
        //Long totalRentals,
        Long totalUnits,
        BigDecimal totalIncome
        //BigDecimal averageIncome
) {
}
