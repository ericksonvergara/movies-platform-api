package com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection;

import java.math.BigDecimal;

public record ClientRankingProjection(
        Long clientId,
        String name,
        String email,
        Long totalRentals,
        BigDecimal totalSpent
) {
}
