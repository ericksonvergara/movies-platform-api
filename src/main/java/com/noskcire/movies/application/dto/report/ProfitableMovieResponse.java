package com.noskcire.movies.application.dto.report;

import java.math.BigDecimal;

public record ProfitableMovieResponse(
        Long movieId,
        String title,
        //Long totalRentals,
        Long totalUnits,
        BigDecimal totalIncome
        //BigDecimal averageIncome
) {
}
