package com.noskcire.movies.infrastructure.adapter.input.rest;

import com.noskcire.movies.application.dto.report.*;
import com.noskcire.movies.application.service.AnalyticsReportService;
import com.noskcire.movies.application.service.RankingReportService;
import com.noskcire.movies.application.service.ReportExportService;
import com.noskcire.movies.application.service.ReportService;
import com.noskcire.movies.application.validation.ValidLimit;
import com.noskcire.movies.domain.enums.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private final ReportExportService reportExportService;

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

    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public StatisticsResponse getStatistics(){
        return analyticsReportService.getStatistics();
    }

    @GetMapping("/rentals/trends")
    @Operation(
            summary = "Consultar tendencias de alquileres",
            description = "Obtiene el comportamiento mensual de alquileres e ingresos dentro de un rango de fechas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Rango de fechas inválido"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public RentalTrendResult getRentalTrends(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        return analyticsReportService.getRentalTrends(startDate, endDate);
    }

    @GetMapping("/export/movies")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<byte[]> exportMovieRanking(
            @RequestParam(defaultValue = "10") @ValidLimit Integer limit
    ) {
        byte[] excel = reportExportService.exportMovieRanking(limit);
        return buildExcelResponse(excel, "ranking_peliculas.xlsx");
    }

    @GetMapping("/export/customers")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<byte[]> exportClientRanking(
            @RequestParam(defaultValue = "10") @ValidLimit Integer limit
    ) {
        byte[] excel = reportExportService.exportClientRanking(limit);
        return buildExcelResponse(excel, "ranking_clientes.xlsx");
    }

    @GetMapping("/export/late-fees")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<byte[]> exportLateFeeRanking(
            @RequestParam(defaultValue = "10") @ValidLimit Integer limit
    ) {
        byte[] excel = reportExportService.exportLateFeeRanking(limit);
        return buildExcelResponse(excel, "ranking_multas.xlsx");
    }

    @GetMapping("/export/reservations")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<byte[]> exportReservationRanking(
            @RequestParam(defaultValue = "10") @ValidLimit Integer limit
    ) {
        byte[] excel = reportExportService.exportReservationRanking(limit);
        return buildExcelResponse(excel, "ranking_reservas.xlsx");
    }

    @GetMapping("/export/income")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<byte[]> exportIncomeByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        byte[] excel = reportExportService.exportIncomeByPeriod(startDate, endDate);
        return buildExcelResponse(excel, "ingresos_periodo.xlsx");
    }

    @GetMapping("/export/rentals")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<byte[]> exportRentalsByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        byte[] excel = reportExportService.exportRentalsByPeriod(startDate, endDate);
        return buildExcelResponse(excel, "alquileres_periodo.xlsx");
    }

    @GetMapping("/export/profitable-movies")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<byte[]> exportProfitableMovies(
            @RequestParam(defaultValue = "10")
            @ValidLimit
            Integer limit
    ) {
        byte[] excel = reportExportService.exportProfitableMovies(limit);
        return buildExcelResponse(excel, "peliculas_rentables.xlsx");
    }

    @GetMapping("/export/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<byte[]> exportStatistics() {
        byte[] excel = reportExportService.exportStatistics();
        return buildExcelResponse(excel, "estadisticas_generales.xlsx");
    }

    private ResponseEntity<byte[]> buildExcelResponse(byte[] excel, String filename) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excel);
    }
}
