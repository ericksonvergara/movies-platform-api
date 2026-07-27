package com.noskcire.movies.application.service;

import com.noskcire.movies.application.dto.report.*;
import com.noskcire.movies.domain.enums.MovieProfitabilitySort;
import com.noskcire.movies.domain.exception.BadRequestException;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.AnalyticsReportRepository;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsReportServiceTest {

    @Mock
    private AnalyticsReportRepository analyticsReportRepository;

    private AnalyticsReportService analyticsReportService;

    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    void setUp() {
        analyticsReportService = new AnalyticsReportService(analyticsReportRepository);
        startDate = LocalDate.of(2025, 1, 1);
        endDate = LocalDate.of(2025, 12, 31);
    }

    @Test
    void getIncomeByPeriod_shouldReturnIncomeByPeriodResponse() {
        var projection = new IncomeByPeriodProjection(
                startDate, endDate, 100L, new BigDecimal("5000.00"), new BigDecimal("50.00")
        );
        when(analyticsReportRepository.getIncomeByPeriod(startDate, endDate))
                .thenReturn(projection);

        IncomeByPeriodResponse response = analyticsReportService.getIncomeByPeriod(startDate, endDate);

        assertNotNull(response);
        assertEquals(startDate, response.startDate());
        assertEquals(endDate, response.endDate());
        assertEquals(100L, response.totalRentals());
        assertEquals(new BigDecimal("5000.00"), response.totalIncome());
        assertEquals(new BigDecimal("50.00"), response.averageRentalAmount());
    }

    @Test
    void getIncomeByPeriod_shouldThrowWhenStartDateAfterEndDate() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> analyticsReportService.getIncomeByPeriod(endDate, startDate)
        );
        assertEquals("La fecha inicial no puede ser mayor que la fecha final", ex.getMessage());
        verifyNoInteractions(analyticsReportRepository);
    }

    @Test
    void getRentalsByPeriod_shouldReturnRentalsByPeriodResult() {
        var rentalDate1 = LocalDate.of(2025, 1, 15);
        var rentalDate2 = LocalDate.of(2025, 2, 10);
        var projections = List.of(
                new RentalsByPeriodProjection(rentalDate1, 5L),
                new RentalsByPeriodProjection(rentalDate2, 8L)
        );
        when(analyticsReportRepository.getRentalsByPeriod(startDate, endDate))
                .thenReturn(projections);

        RentalsByPeriodResult result = analyticsReportService.getRentalsByPeriod(startDate, endDate);

        assertNotNull(result);
        assertEquals(startDate, result.startDate());
        assertEquals(endDate, result.endDate());
        assertEquals(2, result.totalDays());
        assertEquals(2, result.data().size());

        assertEquals(rentalDate1, result.data().get(0).rentalDate());
        assertEquals(5L, result.data().get(0).totalRentals());

        assertEquals(rentalDate2, result.data().get(1).rentalDate());
        assertEquals(8L, result.data().get(1).totalRentals());
    }

    @Test
    void getRentalsByPeriod_shouldReturnEmptyListWhenNoData() {
        when(analyticsReportRepository.getRentalsByPeriod(startDate, endDate))
                .thenReturn(List.of());

        RentalsByPeriodResult result = analyticsReportService.getRentalsByPeriod(startDate, endDate);

        assertNotNull(result);
        assertEquals(0, result.totalDays());
        assertTrue(result.data().isEmpty());
    }

    @Test
    void getRentalsByPeriod_shouldThrowWhenStartDateAfterEndDate() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> analyticsReportService.getRentalsByPeriod(endDate, startDate)
        );
        assertEquals("La fecha inicial no puede ser mayor a la fecha final.", ex.getMessage());
        verifyNoInteractions(analyticsReportRepository);
    }

    @Test
    void getRentalTrends_shouldReturnRentalTrendResult() {
        var projections = List.of(
                new RentalTrendProjection(2025, 1, 30L, new BigDecimal("1500.00")),
                new RentalTrendProjection(2025, 2, 45L, new BigDecimal("2250.00"))
        );
        when(analyticsReportRepository.getRentalTrends(startDate, endDate))
                .thenReturn(projections);

        RentalTrendResult result = analyticsReportService.getRentalTrends(startDate, endDate);

        assertNotNull(result);
        assertEquals(startDate, result.startDate());
        assertEquals(endDate, result.endDate());
        assertEquals(2, result.totalPeriods());
        assertEquals(2, result.data().size());

        assertEquals(2025, result.data().get(0).year());
        assertEquals(1, result.data().get(0).month());
        assertEquals(30L, result.data().get(0).totalRentals());
        assertEquals(new BigDecimal("1500.00"), result.data().get(0).totalIncome());

        assertEquals(2025, result.data().get(1).year());
        assertEquals(2, result.data().get(1).month());
        assertEquals(45L, result.data().get(1).totalRentals());
        assertEquals(new BigDecimal("2250.00"), result.data().get(1).totalIncome());
    }

    @Test
    void getRentalTrends_shouldReturnEmptyListWhenNoData() {
        when(analyticsReportRepository.getRentalTrends(startDate, endDate))
                .thenReturn(List.of());

        RentalTrendResult result = analyticsReportService.getRentalTrends(startDate, endDate);

        assertNotNull(result);
        assertEquals(0, result.totalPeriods());
        assertTrue(result.data().isEmpty());
    }

    @Test
    void getRentalTrends_shouldThrowBadRequestExceptionWhenStartDateAfterEndDate() {
        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> analyticsReportService.getRentalTrends(endDate, startDate)
        );
        assertEquals("La fecha inicial no puede ser mayor a la fecha final.", ex.getMessage());
        verifyNoInteractions(analyticsReportRepository);
    }

    @Test
    void getProfitableMovie_shouldReturnProfitableMovieResult() {
        var projections = List.of(
                new ProfitableMovieProjection(1L, "Movie A", 50L, new BigDecimal("2500.00")),
                new ProfitableMovieProjection(2L, "Movie B", 30L, new BigDecimal("1500.00"))
        );
        when(analyticsReportRepository.getMostProfitableMovies(5, MovieProfitabilitySort.TOTAL_INCOME))
                .thenReturn(projections);

        ProfitableMovieResult result = analyticsReportService.getProfitableMovie(
                5, MovieProfitabilitySort.TOTAL_INCOME
        );

        assertNotNull(result);
        assertEquals(5, result.limit());
        assertEquals(MovieProfitabilitySort.TOTAL_INCOME, result.sortBy());
        assertEquals(2, result.totalResult());
        assertEquals(2, result.data().size());

        assertEquals(1L, result.data().get(0).movieId());
        assertEquals("Movie A", result.data().get(0).title());
        assertEquals(50L, result.data().get(0).totalUnits());
        assertEquals(new BigDecimal("2500.00"), result.data().get(0).totalIncome());

        assertEquals(2L, result.data().get(1).movieId());
        assertEquals("Movie B", result.data().get(1).title());
        assertEquals(30L, result.data().get(1).totalUnits());
        assertEquals(new BigDecimal("1500.00"), result.data().get(1).totalIncome());
    }

    @Test
    void getProfitableMovie_shouldDefaultLimitTo10WhenNull() {
        when(analyticsReportRepository.getMostProfitableMovies(10, MovieProfitabilitySort.TOTAL_INCOME))
                .thenReturn(List.of());

        ProfitableMovieResult result = analyticsReportService.getProfitableMovie(null, MovieProfitabilitySort.TOTAL_INCOME);

        assertEquals(10, result.limit());
    }

    @Test
    void getProfitableMovie_shouldDefaultLimitTo10WhenZero() {
        when(analyticsReportRepository.getMostProfitableMovies(10, MovieProfitabilitySort.TOTAL_RENTALS))
                .thenReturn(List.of());

        ProfitableMovieResult result = analyticsReportService.getProfitableMovie(0, MovieProfitabilitySort.TOTAL_RENTALS);

        assertEquals(10, result.limit());
    }

    @Test
    void getProfitableMovie_shouldDefaultLimitTo10WhenNegative() {
        when(analyticsReportRepository.getMostProfitableMovies(10, MovieProfitabilitySort.TOTAL_INCOME))
                .thenReturn(List.of());

        ProfitableMovieResult result = analyticsReportService.getProfitableMovie(-5, MovieProfitabilitySort.TOTAL_INCOME);

        assertEquals(10, result.limit());
    }

    @Test
    void getProfitableMovie_shouldDefaultSortByToTotalIncomeWhenNull() {
        when(analyticsReportRepository.getMostProfitableMovies(10, MovieProfitabilitySort.TOTAL_INCOME))
                .thenReturn(List.of());

        ProfitableMovieResult result = analyticsReportService.getProfitableMovie(10, null);

        assertEquals(MovieProfitabilitySort.TOTAL_INCOME, result.sortBy());
    }

    @Test
    void getProfitableMovie_shouldReturnEmptyListWhenNoData() {
        when(analyticsReportRepository.getMostProfitableMovies(10, MovieProfitabilitySort.TOTAL_INCOME))
                .thenReturn(List.of());

        ProfitableMovieResult result = analyticsReportService.getProfitableMovie(10, MovieProfitabilitySort.TOTAL_INCOME);

        assertEquals(0, result.totalResult());
        assertTrue(result.data().isEmpty());
    }

    @Test
    void getStatistics_shouldReturnStatisticsResponse() {
        var projection = new StatisticsProjection(
                new BigDecimal("50000.00"), 1000L, 200L, 50L, 300L,
                "Movie A", "Juan Perez"
        );
        when(analyticsReportRepository.getStatistics()).thenReturn(projection);

        StatisticsResponse response = analyticsReportService.getStatistics();

        assertNotNull(response);
        assertEquals(new BigDecimal("50000.00"), response.totalIncome());
        assertEquals(1000L, response.totalRentals());
        assertEquals(200L, response.totalReservations());
        assertEquals(50L, response.totalLateFees());
        assertEquals(300L, response.totalPersons());
        assertEquals("Movie A", response.mostRentedMovie());
        assertEquals("Juan Perez", response.topPerson());
    }
}
