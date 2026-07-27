package com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report;

import com.noskcire.movies.domain.enums.MovieProfitabilitySort;
import com.noskcire.movies.domain.enums.RentalStatus;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.IncomeByPeriodProjection;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.ProfitableMovieProjection;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.RentalTrendProjection;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.RentalsByPeriodProjection;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.StatisticsProjection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AnalyticsReportRepositoryImpl implements AnalyticsReportRepository {

    private static final String INCOME_BY_PERIOD_PROJECTION =
            IncomeByPeriodProjection.class.getCanonicalName();

    private static final String RENTAL_BY_PERIOD_PROJECTION =
            RentalsByPeriodProjection.class.getCanonicalName();

    private static final String PROFITABLE_MOVIE_PROJECTION =
            ProfitableMovieProjection.class.getCanonicalName();

    private static final String RENTAL_TREND_PROJECTION =
            RentalTrendProjection.class.getCanonicalName();

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public IncomeByPeriodProjection getIncomeByPeriod(LocalDate startDate, LocalDate endDate) {
        String jpql = """
                    SELECT 
                        COUNT(r.id),
                        COALESCE(SUM(r.total),0),
                        COALESCE(AVG(r.total),0)
                    FROM Rental r
                    WHERE r.rentalDate BETWEEN :startDate AND :endDate
                    AND r.status IN(
                        :activeStatus,
                        :returnedStatus,
                        :overdueStatus
                    )
                """;

        Object[] result = (Object[]) entityManager
                .createQuery(jpql)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("activeStatus", RentalStatus.ACTIVE)
                .setParameter("returnedStatus", RentalStatus.RETURNED)
                .setParameter("overdueStatus", RentalStatus.OVERDUE)
                .getSingleResult();

        return new IncomeByPeriodProjection(
                startDate,
                endDate,
                ((Number) result[0]).longValue(),
                java.math.BigDecimal.valueOf(((Number) result[1]).doubleValue()),
                java.math.BigDecimal.valueOf(((Number) result[2]).doubleValue())
        );
    }

    @Override
    public List<RentalsByPeriodProjection> getRentalsByPeriod(LocalDate startDate, LocalDate endDate) {
        String jpql = String.format("""
                        SELECT NEW %s(
                        r.rentalDate,
                        COUNT(r.id)
                        )
                        From Rental r
                        WHERE r.rentalDate BETWEEN :startDate AND :endDate
                        AND r.status IN (
                            :activeStatus,
                            :returnedStatus,
                            :overdueStatus
                        )
                        GROUP BY
                        r.rentalDate
                        ORDER BY
                        r.rentalDate ASC
                        """,
                RENTAL_BY_PERIOD_PROJECTION
        );
        return entityManager
                .createQuery(
                        jpql,
                        RentalsByPeriodProjection.class
                )
                .setParameter("startDate",
                        startDate
                )
                .setParameter("endDate",
                        endDate
                )
                .setParameter("activeStatus",
                        RentalStatus.ACTIVE
                )
                .setParameter("returnedStatus",
                        RentalStatus.RETURNED
                )
                .setParameter("overdueStatus",
                        RentalStatus.OVERDUE
                )
                .getResultList();
    }

    @Override
    public List<RentalTrendProjection> getRentalTrends(LocalDate startDate, LocalDate endDate) {
        String jpql = String.format("""
                        SELECT NEW %s(
                        YEAR(r.rentalDate),
                        MONTH(r.rentalDate),
                        COUNT(r.id),
                        COALESCE(SUM(r.total), 0)
                        )
                        FROM Rental r
                        WHERE r.rentalDate BETWEEN :startDate AND :endDate
                        AND r.status IN (
                            :activeStatus,
                            :returnedStatus,
                            :overdueStatus
                        )
                        GROUP BY YEAR(r.rentalDate), MONTH(r.rentalDate)
                        ORDER BY YEAR(r.rentalDate) ASC, MONTH(r.rentalDate) ASC
                        """,
                RENTAL_TREND_PROJECTION
        );
        return entityManager
                .createQuery(jpql, RentalTrendProjection.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("activeStatus", RentalStatus.ACTIVE)
                .setParameter("returnedStatus", RentalStatus.RETURNED)
                .setParameter("overdueStatus", RentalStatus.OVERDUE)
                .getResultList();
    }

    @Override
    public List<ProfitableMovieProjection> getMostProfitableMovies(Integer limit, MovieProfitabilitySort sortBy) {
        String jpql = String.format("""
                        SELECT NEW %s(
                        
                            m.id,
                        
                            m.title,
                        
                            SUM(rd.quantity),
                        
                            SUM(rd.rentalPrice * rd.quantity)
                        
                        )
                        FROM RentalDetail rd
                        
                        JOIN rd.movie m
                        
                        JOIN rd.rental r
                        
                        WHERE r.status IN (
                        
                            :activeStatus,
                        
                            :returnedStatus,
                        
                            :overdueStatus
                        
                        )
                        
                        GROUP BY
                        
                            m.id,
                        
                            m.title
                        
                        ORDER BY
                        
                            %s DESC
                        """,
                PROFITABLE_MOVIE_PROJECTION,
                resolveSort(sortBy)
        );

        return entityManager
                .createQuery(
                        jpql,
                        ProfitableMovieProjection.class
                )
                .setParameter(
                        "activeStatus",
                        RentalStatus.ACTIVE
                )
                .setParameter(
                        "returnedStatus",
                        RentalStatus.RETURNED
                )
                .setParameter(
                        "overdueStatus",
                        RentalStatus.OVERDUE
                )
                .setMaxResults(limit)
                .getResultList();
    }

    private String resolveSort(MovieProfitabilitySort sortBy) {

        return switch (sortBy) {

            case TOTAL_INCOME -> "SUM(rd.rentalPrice * rd.quantity)";

            case TOTAL_RENTALS -> "SUM(rd.quantity)";

            case AVERAGE_INCOME -> "AVG(rd.rentalPrice * rd.quantity)";
        };
    }

    @Override
    public StatisticsProjection getStatistics() {

        return new StatisticsProjection(
                getTotalIncome(),
                getTotalRentals(),
                getTotalReservations(),
                getTotalLateFees(),
                getActivePersons(),
                getMostRentedMovie(),
                topPerson()
        );
    }

    private BigDecimal getTotalIncome() {
        BigDecimal total =
                entityManager.createQuery("""
                                        SELECT COALESCE(SUM (rd.rentalPrice * rd.quantity), 0)
                                        FROM RentalDetail rd
                                        JOIN rd.rental r
                                        WHERE r.status IN (
                                        :activeStatus,
                                        :returnedStatus,
                                        :overdueStatus
                                        )
                                        """,
                                BigDecimal.class)
                        .setParameter("activeStatus",
                                RentalStatus.ACTIVE
                        )
                        .setParameter("returnedStatus",
                                RentalStatus.RETURNED
                        )
                        .setParameter("overdueStatus",
                                RentalStatus.OVERDUE
                        )
                        .getSingleResult();

        return total == null ?
                BigDecimal.ZERO : total;
    }


    private Long getTotalRentals() {
        Long total = entityManager.createQuery(
                """
                        SELECT COUNT(r.id)
                        FROM Rental r
                        WHERE r.status IN (
                        :activeStatus,
                        :returnedStatus,
                        :overdueStatus
                        )
                        """,
                Long.class
        )
                .setParameter("activeStatus",
                        RentalStatus.ACTIVE
                )
                .setParameter("returnedStatus",
                        RentalStatus.RETURNED
                )
                .setParameter("overdueStatus",
                        RentalStatus.OVERDUE
                )
                .getSingleResult();

        return total == null ? 0L : total;
    }

    private Long getTotalReservations() {
        Long total = entityManager.createQuery(
                        """
                                SELECT COUNT(r.id)
                                FROM Reservation r
                                """, Long.class
                )
                .getSingleResult();
        return total == null ? 0L : total;
    }

    private Long getTotalLateFees() {
        Long total = entityManager.createQuery(
                        """
                                SELECT COUNT(l.id)
                                FROM LateFee l
                                """, Long.class
                )
                .getSingleResult();

        return total == null ? 0L : total;
    }
    private Long getActivePersons() {
        Long total = entityManager.createQuery(
                        """
                                SELECT COUNT(p.id)
                                FROM Person p
                                """, Long.class
                )
                .getSingleResult();

        return total == null ? 0L : total;
    }

    private String getMostRentedMovie(){

        List<String> result = entityManager.createQuery(
                        """
                                SELECT m.title
                                FROM RentalDetail rd
                                JOIN rd.movie m
                                JOIN rd.rental r
                                WHERE r.status IN (
                                :activeStatus,
                                :returnedStatus,
                                :overdueStatus
                                )
                                GROUP BY m.id, m.title
                                ORDER BY SUM(rd.quantity) DESC
                                """, String.class)
                .setParameter("activeStatus",
                        RentalStatus.ACTIVE
                )
                .setParameter("returnedStatus",
                        RentalStatus.RETURNED
                )
                .setParameter("overdueStatus",
                        RentalStatus.OVERDUE
                )
                .setMaxResults(1)
                .getResultList();

        return result.isEmpty() ? null : result.get(0);
    }

    private String topPerson() {
        List<String> top = entityManager.createQuery(
                        """
                                SELECT CONCAT(c.names,' ',c.lastNames)
                                FROM Rental r
                                JOIN r.client c
                                WHERE r.status IN (
                                    :activeStatus,
                                    :returnedStatus,
                                    :overdueStatus
                                )
                                GROUP BY c.id, c.names, c.lastNames
                                ORDER BY COUNT(r.id) DESC
                                """, String.class
                )
                .setParameter("activeStatus",
                        RentalStatus.ACTIVE
                )
                .setParameter("returnedStatus",
                        RentalStatus.RETURNED
                )
                .setParameter("overdueStatus",
                        RentalStatus.OVERDUE
                )
                .setMaxResults(1)
                .getResultList();

        return top.isEmpty() ? null : top.get(0);

    }
}