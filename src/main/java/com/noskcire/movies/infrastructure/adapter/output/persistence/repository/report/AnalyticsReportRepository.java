package com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report;

import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.IncomeByPeriodProjection;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.RentalsByPeriodProjection;

import java.time.LocalDate;
import java.util.List;

public interface AnalyticsReportRepository {
    IncomeByPeriodProjection getIncomeByPeriod(
            LocalDate startDate,
            LocalDate endDate
    );

    List<RentalsByPeriodProjection> getRentalsByPeriod(
            LocalDate startDate,
            LocalDate endDate
    );
}
