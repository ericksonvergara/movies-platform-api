package com.noskcire.movies.application.dto.rental;

import com.noskcire.movies.domain.enums.RentalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record RentalResponse(
        Long id,
        Long clientId,
        String clientName,
        String employeeUsername,
        LocalDate rentalDate,
        LocalDate expectedReturnDate,
        LocalDateTime returnedDate,
        RentalStatus status,
        BigDecimal total,
        List<RentalDetailResponse> details
) {
}
