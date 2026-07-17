package com.noskcire.movies.application.dto.report;

import java.math.BigDecimal;

public record ClientRankingResponse(
        Long clientId,
        String name,
        String email,
        Long totalRentals,
        BigDecimal totalSpent
) {
}
