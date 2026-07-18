package com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection;

import java.math.BigDecimal;

public record LateFeeRankingProjection(
        Long clientId,
        String clientName,
        Long totalLateFees,
        Long pendingLateFees,
        Long paidLateFees,
        BigDecimal totalAmount
) {
}
