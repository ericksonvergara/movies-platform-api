package com.noskcire.movies.application.dto.report;

import java.time.LocalDate;
import java.util.List;

public record RentalTrendResult(
        LocalDate startDate,
        LocalDate endDate,
        Integer totalPeriods,
        List<RentalTrendResponse> data
) {
}
