package com.noskcire.movies.application.dto.rental;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OverdueRentalResponse(
        Long rentalId,
        Long clientId,
        String clientName,
        LocalDate expectedReturnDate,
        Long daysLate,
        BigDecimal total
) {
}
