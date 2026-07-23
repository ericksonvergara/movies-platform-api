package com.noskcire.movies.infrastructure.adapter.input.rest;

import com.noskcire.movies.application.dto.report.*;
import com.noskcire.movies.application.service.AnalyticsReportService;
import com.noskcire.movies.application.service.RankingReportService;
import com.noskcire.movies.application.service.ReportService;
import com.noskcire.movies.application.validation.ValidLimit;
import com.noskcire.movies.domain.enums.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Validated
public class ReportController {

    private final ReportService reportService;
    private final RankingReportService rankingReportService;
    private final AnalyticsReportService analyticsReportService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public DashboardResponse dashboard() {
        return reportService.dashboard();
    }

    @GetMapping("/rankings/movies")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public MovieRankingResult getMovieRanking(
            @RequestParam(defaultValue = "10")
            @ValidLimit
            Integer limit,

            @RequestParam(defaultValue = "RENTALS")
            MovieRankingSort sort
    ) {
        return rankingReportService.getMovieRanking(limit, sort);
    }

    @GetMapping("/rankings/clients")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ClientRankingResult getClientRanking(
            @RequestParam(defaultValue = "10")
            @ValidLimit
            Integer limit,
            @RequestParam(defaultValue = "RENTALS")
            ClientRankingSort sort
    ) {
        return rankingReportService.getClientRanking(limit, sort);
    }

    @GetMapping("/rankings/late-fees")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public LateFeeRankingResult getLateFeeRanking(
            @RequestParam(defaultValue = "10")  
            @ValidLimit
            Integer limit,

            @RequestParam(defaultValue = "COUNT")
            LateFeeRankingSort sort
    ) {

        return rankingReportService.getLateFeeRanking(
                limit,
                sort
        );
    }

    @GetMapping("/rankings/reservations")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ReservationRankingResult getReservationRanking(
            @RequestParam(defaultValue = "10")
            @ValidLimit
            Integer limit,

            @RequestParam(defaultValue = "COUNT")
            ReservationRankingSort sort


    ){

        return rankingReportService.getReservationRanking(limit, sort);
    }

    @GetMapping("/rankings/income")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public IncomeByPeriodResponse getIncomeByPeriod(
            LocalDate startDate,
            LocalDate endDate

    ){
        return analyticsReportService.getIncomeByPeriod(startDate, endDate);
    }

    @GetMapping("/rentals")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public RentalsByPeriodResult rentalsByPeriod(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ){
        return analyticsReportService.getRentalsByPeriod(
                startDate,
                endDate
        );
    }

    @GetMapping("/movies/profitability")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ProfitableMovieResult getProfitableMovie(
            @RequestParam(defaultValue = "10")
            Integer limit,
            
            @RequestParam(defaultValue = "TOTAL_INCOME")
            MovieProfitabilitySort sortBy
    ){
        return analyticsReportService.getProfitableMovie(limit, sortBy);
    }


}
