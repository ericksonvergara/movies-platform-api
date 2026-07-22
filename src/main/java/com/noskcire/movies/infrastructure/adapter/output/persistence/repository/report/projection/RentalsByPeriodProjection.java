package com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection;

import java.time.LocalDate;

public record RentalsByPeriodProjection(
        LocalDate rentalDate,
        Long totalRentals
) {
}
