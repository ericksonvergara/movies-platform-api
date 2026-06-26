package com.noskcire.movies.application.dto.lateFee;

import com.noskcire.movies.domain.enums.LateFeeStatus;

import java.math.BigDecimal;

public record LateFeeResponse(
        Long id,
        Long rentalId,
        Long clientId,
        String clientName,
        Long daysLate,
        BigDecimal dailyAmount,
        BigDecimal totalAmount,
        LateFeeStatus status
) {
}
