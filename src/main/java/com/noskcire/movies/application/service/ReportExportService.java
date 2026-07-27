package com.noskcire.movies.application.service;

import com.noskcire.movies.domain.enums.*;
import com.noskcire.movies.infrastructure.adapter.output.report.ExcelReportExporter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportExportService {

    private final AnalyticsReportService analyticsReportService;
    private final RankingReportService rankingReportService;
    private final ExcelReportExporter excelReportExporter;

    public byte[] exportMovieRanking(Integer limit) {
        var result = rankingReportService.getMovieRanking(limit, MovieRankingSort.RENTALS);
        return excelReportExporter.exportMovieRanking(result);
    }

    public byte[] exportClientRanking(Integer limit) {
        var result = rankingReportService.getClientRanking(limit, ClientRankingSort.RENTALS);
        return excelReportExporter.exportClientRanking(result);
    }

    public byte[] exportLateFeeRanking(Integer limit) {
        var result = rankingReportService.getLateFeeRanking(limit, LateFeeRankingSort.COUNT);
        return excelReportExporter.exportLateFeeRanking(result);
    }

    public byte[] exportReservationRanking(Integer limit) {
        var result = rankingReportService.getReservationRanking(limit, ReservationRankingSort.COUNT);
        return excelReportExporter.exportReservationRanking(result);
    }

    public byte[] exportIncomeByPeriod(LocalDate startDate, LocalDate endDate) {
        var result = analyticsReportService.getIncomeByPeriod(startDate, endDate);
        return excelReportExporter.exportIncomeByPeriod(result);
    }

    public byte[] exportRentalsByPeriod(LocalDate startDate, LocalDate endDate) {
        var result = analyticsReportService.getRentalsByPeriod(startDate, endDate);
        return excelReportExporter.exportRentalsByPeriod(result);
    }

    public byte[] exportProfitableMovies(Integer limit) {
        var result = analyticsReportService.getProfitableMovie(limit, MovieProfitabilitySort.TOTAL_INCOME);
        return excelReportExporter.exportProfitableMovies(result);
    }

    public byte[] exportStatistics() {
        var result = analyticsReportService.getStatistics();
        return excelReportExporter.exportStatistics(result);
    }
}
