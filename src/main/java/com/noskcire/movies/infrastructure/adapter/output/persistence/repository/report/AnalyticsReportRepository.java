package com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report;

import com.noskcire.movies.domain.enums.MovieProfitabilitySort;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.IncomeByPeriodProjection;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.ProfitableMovieProjection;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.RentalTrendProjection;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.RentalsByPeriodProjection;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.StatisticsProjection;

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

    List<RentalTrendProjection> getRentalTrends(
            LocalDate startDate,
            LocalDate endDate
    );

    List<ProfitableMovieProjection> getMostProfitableMovies(
            Integer limit,
            MovieProfitabilitySort sortBy
    );

    StatisticsProjection getStatistics();
}
