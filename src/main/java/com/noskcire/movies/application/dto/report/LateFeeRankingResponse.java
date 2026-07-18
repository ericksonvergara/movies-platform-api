package com.noskcire.movies.application.dto.report;

import java.math.BigDecimal;

public record LateFeeRankingResponse(
        Long clientId,
        String clientName,
        Long totalLateFees,
        Long pendingLateFees,
        Long paidLateFees,
        BigDecimal totalAmount
) {
}
