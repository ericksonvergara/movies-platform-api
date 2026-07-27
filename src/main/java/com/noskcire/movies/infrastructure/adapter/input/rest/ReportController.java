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

    @Operation(summary = "Obtener dashboard", description = "Devuelve un resumen con indicadores clave del sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dashboard obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public DashboardResponse dashboard() {
        return reportService.dashboard();
    }

    @Operation(summary = "Ranking de películas", description = "Obtiene el ranking de películas más alquiladas o mejor valoradas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ranking obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
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

    @Operation(summary = "Ranking de clientes", description = "Obtiene el ranking de clientes con más alquileres o multas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ranking obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
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

    @Operation(summary = "Ranking de multas", description = "Obtiene el ranking de multas más frecuentes o de mayor monto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ranking obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
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

    @Operation(summary = "Ranking de reservas", description = "Obtiene el ranking de películas y clientes con más reservas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ranking obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
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

    @Operation(summary = "Ingresos por período", description = "Obtiene el resumen de ingresos generados en un rango de fechas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ingresos obtenidos correctamente"),
            @ApiResponse(responseCode = "400", description = "Rango de fechas inválido"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/rankings/income")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public IncomeByPeriodResponse getIncomeByPeriod(
            LocalDate startDate,
            LocalDate endDate

    ){
        return analyticsReportService.getIncomeByPeriod(startDate, endDate);
    }

    @Operation(summary = "Alquileres por período", description = "Obtiene el resumen de alquileres realizados en un rango de fechas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alquileres obtenidos correctamente"),
            @ApiResponse(responseCode = "400", description = "Rango de fechas inválido"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
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

    @Operation(summary = "Películas más rentables", description = "Obtiene el ranking de películas más rentables según los ingresos generados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ranking obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
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

    @Operation(summary = "Estadísticas generales", description = "Obtiene estadísticas generales del sistema como totales y promedios")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
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

    @Operation(summary = "Exportar ranking de películas", description = "Exporta a Excel el ranking de películas más alquiladas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Archivo Excel generado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/export/movies")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<byte[]> exportMovieRanking(
            @RequestParam(defaultValue = "10") @ValidLimit Integer limit
    ) {
        byte[] excel = reportExportService.exportMovieRanking(limit);
        return buildExcelResponse(excel, "ranking_peliculas.xlsx");
    }

    @Operation(summary = "Exportar ranking de clientes", description = "Exporta a Excel el ranking de clientes con más actividad")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Archivo Excel generado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/export/customers")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<byte[]> exportClientRanking(
            @RequestParam(defaultValue = "10") @ValidLimit Integer limit
    ) {
        byte[] excel = reportExportService.exportClientRanking(limit);
        return buildExcelResponse(excel, "ranking_clientes.xlsx");
    }

    @Operation(summary = "Exportar ranking de multas", description = "Exporta a Excel el ranking de multas por cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Archivo Excel generado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/export/late-fees")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<byte[]> exportLateFeeRanking(
            @RequestParam(defaultValue = "10") @ValidLimit Integer limit
    ) {
        byte[] excel = reportExportService.exportLateFeeRanking(limit);
        return buildExcelResponse(excel, "ranking_multas.xlsx");
    }

    @Operation(summary = "Exportar ranking de reservas", description = "Exporta a Excel el ranking de reservas por película o cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Archivo Excel generado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/export/reservations")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<byte[]> exportReservationRanking(
            @RequestParam(defaultValue = "10") @ValidLimit Integer limit
    ) {
        byte[] excel = reportExportService.exportReservationRanking(limit);
        return buildExcelResponse(excel, "ranking_reservas.xlsx");
    }

    @Operation(summary = "Exportar ingresos por período", description = "Exporta a Excel el resumen de ingresos en un rango de fechas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Archivo Excel generado correctamente"),
            @ApiResponse(responseCode = "400", description = "Rango de fechas inválido"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/export/income")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<byte[]> exportIncomeByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        byte[] excel = reportExportService.exportIncomeByPeriod(startDate, endDate);
        return buildExcelResponse(excel, "ingresos_periodo.xlsx");
    }

    @Operation(summary = "Exportar alquileres por período", description = "Exporta a Excel el resumen de alquileres en un rango de fechas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Archivo Excel generado correctamente"),
            @ApiResponse(responseCode = "400", description = "Rango de fechas inválido"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/export/rentals")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<byte[]> exportRentalsByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        byte[] excel = reportExportService.exportRentalsByPeriod(startDate, endDate);
        return buildExcelResponse(excel, "alquileres_periodo.xlsx");
    }

    @Operation(summary = "Exportar películas más rentables", description = "Exporta a Excel el ranking de películas más rentables")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Archivo Excel generado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
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

    @Operation(summary = "Exportar estadísticas generales", description = "Exporta a Excel las estadísticas generales del sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Archivo Excel generado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
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
