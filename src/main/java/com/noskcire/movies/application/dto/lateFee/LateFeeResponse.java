package com.noskcire.movies.application.dto.lateFee;

import com.noskcire.movies.domain.enums.LateFeeStatus;
import com.noskcire.movies.domain.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LateFeeResponse(
        Long id,
        Long rentalId,
        Long clientId,
        String clientName,
        Long daysLate,
        BigDecimal dailyAmount,
        BigDecimal totalAmount,
        LateFeeStatus status,
        LocalDateTime paymentDate,
        PaymentMethod paymentMethod,
        String observations
) {
}
