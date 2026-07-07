package com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection;

import java.math.BigDecimal;

public record MovieRankingProjection(
        Long movieId,
        String title,
        Long timesRented,
        Long unitsRented,
        Double averageUnits,
        BigDecimal revenue
) {
}
