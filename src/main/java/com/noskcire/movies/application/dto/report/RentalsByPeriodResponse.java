package com.noskcire.movies.application.dto.report;

import java.time.LocalDate;

public record RentalsByPeriodResponse(
        LocalDate rentalDate,
        Long totalRentals
) {
}
