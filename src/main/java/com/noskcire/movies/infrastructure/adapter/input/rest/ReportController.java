package com.noskcire.movies.infrastructure.adapter.input.rest;

import com.noskcire.movies.application.dto.report.DashboardResponse;
import com.noskcire.movies.application.dto.report.MovieRankingResponse;
import com.noskcire.movies.application.dto.report.MovieRankingResult;
import com.noskcire.movies.application.service.RankingReportService;
import com.noskcire.movies.application.service.ReportService;
import com.noskcire.movies.application.validation.ValidLimit;
import com.noskcire.movies.domain.enums.MovieRankingSort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Validated
public class ReportController {

    private final ReportService reportService;
    private final RankingReportService rankingReportService;

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
}
