package com.noskcire.movies.application.dto.report;

import java.math.BigDecimal;

public record MovieRankingResponse(
        Long movieId,
        String title,
        Long timesRented,
        Long unitsRented,
        Double averageUnits,
        BigDecimal revenue
) {
}
