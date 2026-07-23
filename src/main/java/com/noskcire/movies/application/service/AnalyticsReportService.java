package com.noskcire.movies.application.service;

import com.noskcire.movies.application.dto.report.IncomeByPeriodResponse;
import com.noskcire.movies.application.dto.report.RentalsByPeriodResponse;
import com.noskcire.movies.application.dto.report.RentalsByPeriodResult;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.AnalyticsReportRepository;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.IncomeByPeriodProjection;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.RentalsByPeriodProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsReportService {

    private final AnalyticsReportRepository analyticsReportRepository;

    public IncomeByPeriodResponse getIncomeByPeriod(
            LocalDate startDate,
            LocalDate endDate

    ){
        if (startDate.isAfter(endDate)){
            throw new
                    IllegalArgumentException(
                    "La fecha inicial no puede ser mayor que la fecha final");
        }

        IncomeByPeriodProjection report =
                analyticsReportRepository.getIncomeByPeriod(
                        startDate,
                        endDate
                );

        return new IncomeByPeriodResponse(
                startDate,
                endDate,
                report.totalRentals(),
                report.totalIncome(),
                report.averageRentalAmount()
        );
    }

    public RentalsByPeriodResult getRentalsByPeriod(
            LocalDate startDate,
            LocalDate endDate


    ){
        if (startDate.isAfter(endDate)){
            throw new
                    IllegalArgumentException(
                    "La fecha inicial no puede ser mayor a la fecha final.");
        }

        List<RentalsByPeriodProjection> reports =
                analyticsReportRepository.getRentalsByPeriod(
                        startDate,
                        endDate
                );

        List<RentalsByPeriodResponse> data =
                reports.stream()
                        .map(report -> new RentalsByPeriodResponse(
                                report.rentalDate(),
                                report.totalRentals())
                        )
                        .toList();

        return new RentalsByPeriodResult(
                startDate,
                endDate,
                data.size(),
                data
        );
    }
}
