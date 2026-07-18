package com.noskcire.movies.application.dto.report;

import java.math.BigDecimal;

public record LateFeeRankingResponse(
        Long clientId,
        String clientName,
        Long lateFees,
        Long pending,
        Long paid,
        BigDecimal totalAmount
) {
}
