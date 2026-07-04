package com.noskcire.movies.application.dto.report;

import java.math.BigDecimal;

public record RevenueDashboardResponse(

        BigDecimal rentals,
        BigDecimal lateFees,
        BigDecimal total
) {
}
