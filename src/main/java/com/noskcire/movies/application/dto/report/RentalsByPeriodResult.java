package com.noskcire.movies.application.dto.report;

import java.time.LocalDate;
import java.util.List;

public record RentalsByPeriodResult(
        LocalDate startDate,
        LocalDate endDate,
        Integer totalDays,
        List<RentalsByPeriodResponse> data
) {
}
