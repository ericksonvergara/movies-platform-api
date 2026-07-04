package com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection;

import java.math.BigDecimal;

public record RevenueSummary(

        BigDecimal rentals,

        BigDecimal lateFees
) {
}
