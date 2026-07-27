package com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report;

import com.noskcire.movies.domain.enums.*;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RankingReportRepositoryImpl
        implements RankingReportRepository {

    private static final String MOVIE_RANKING_PROJECTION =
            MovieRankingProjection.class.getCanonicalName();

    private static final String CLIENT_RANKING_PROJECTION =
            ClientRankingProjection.class.getCanonicalName();

    private static final String LATEFEE_RANKING_PROJECTION =
            LateFeeRankingProjection.class.getCanonicalName();

    private static final String RESERVATION_RANKING_PROJECTION =
            ReservationRankingProjection.class.getCanonicalName();

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MovieRankingProjection> getMovieRanking(
            Integer limit,
            MovieRankingSort sort
    ) {

        String jpql = String.format("""
                        SELECT NEW %s(
                            m.id,
                            m.title,
                            COUNT(DISTINCT r.id),
                            SUM(rd.quantity),
                            AVG(rd.quantity),
                            SUM(rd.quantity * rd.rentalPrice)
                        )
                        FROM RentalDetail rd
                        JOIN rd.movie m
                        JOIN rd.rental r
                        WHERE r.status = :status
                        GROUP BY
                            m.id,
                            m.title
                        ORDER BY %s DESC
                        """,
                MOVIE_RANKING_PROJECTION,
                sort.getExpression()
        );

        return entityManager
                .createQuery(
                        jpql,
                        MovieRankingProjection.class
                )
                .setParameter(
                        "status",
                        RentalStatus.RETURNED
                )
                .setMaxResults(limit)
                .getResultList();

    }

    @Override
    public List<ClientRankingProjection> getClientRanking(Integer limit, ClientRankingSort sort) {
        String jpql = String.format("""
                        SELECT NEW %s(
                            p.id,
                            CONCAT(p.names, ' ', p.lastNames),
                            p.email,
                            COUNT(DISTINCT r.id),
                            SUM(rd.quantity * rd.rentalPrice)
                        )
                        FROM RentalDetail rd
                        JOIN rd.rental r
                        JOIN r.client p
                        WHERE r.status = :status
                        GROUP BY
                            p.id,
                            p.names,
                            p.lastNames,
                            p.email
                        ORDER BY %s DESC
                        """,
                CLIENT_RANKING_PROJECTION,
                sort.getExpression()
        );

        return entityManager
                .createQuery(
                        jpql,
                        ClientRankingProjection.class
                )
                .setParameter(
                        "status",
                        RentalStatus.RETURNED
                )
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<LateFeeRankingProjection> getLateFeeRanking(Integer limit, LateFeeRankingSort sort) {
        String jpql = String.format("""
                        SELECT NEW %s(
                            p.id,
                            CONCAT (p.names, ' ', p.lastNames),
                            COUNT(lf.id),
                            SUM(
                                CASE
                                    WHEN lf.status = :activeStatus
                                    THEN 1
                                    ELSE 0
                                END
                            ),
                            SUM(
                                CASE
                                    WHEN lf.status= :pendingStatus
                                    THEN 1
                                    ELSE 0
                                END
                            ),
                            SUM(
                                CASE
                                    WHEN lf.status= :paidStatus
                                    THEN 1
                                    ELSE 0
                                END
                            ),
                            COALESCE(SUM(lf.totalAmount), 0)
                        )
                        FROM LateFee lf
                        JOIN lf.rental r
                        JOIN r.client p
                        GROUP BY
                            p.id,
                            p.names,
                            p.lastNames
                        ORDER BY %s DESC
                        """,
                LATEFEE_RANKING_PROJECTION,
                sort.getExpression()
        );

        return entityManager
                .createQuery(
                        jpql,
                        LateFeeRankingProjection.class
                )
                .setParameter(
                        "activeStatus",
                        LateFeeStatus.ACTIVE
                )
                .setParameter(
                        "pendingStatus",
                        LateFeeStatus.PENDING
                )
                .setParameter(
                        "paidStatus",
                        LateFeeStatus.PAID
                )
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<ReservationRankingProjection> getReservationRanking(Integer limit, ReservationRankingSort sort) {
        String jpql = String.format("""
                SELECT NEW %s(
                
                    p.id,
                
                    CONCAT(p.names,' ',p.lastNames),
                
                    COUNT(r.id),
                
                    SUM(
                        CASE
                            WHEN r.status = :activeStatus
                            THEN 1
                            ELSE 0
                        END
                    ),
                
                    SUM(
                        CASE
                            WHEN r.status = :notifiedStatus
                            THEN 1
                            ELSE 0
                        END
                    ),
                
                    SUM(
                        CASE
                            WHEN r.status = :fulfilledStatus
                            THEN 1
                            ELSE 0
                        END
                    ),
                
                    SUM(
                        CASE
                            WHEN r.status = :cancelledStatus
                            THEN 1
                            ELSE 0
                        END
                    ),
                
                    SUM(
                        CASE
                            WHEN r.status = :expiredStatus
                            THEN 1
                            ELSE 0
                        END
                    )
                
                )
                
                FROM Reservation r
                
                JOIN r.client p
                
                GROUP BY
                
                    p.id,
                    p.names,
                    p.lastNames
                
                ORDER BY %s DESC
                """,
                RESERVATION_RANKING_PROJECTION,
                sort.getExpression()
        );
        return entityManager
                .createQuery(
                        jpql,
                        ReservationRankingProjection.class
                )
                .setParameter("activeStatus",
                        ReservationStatus.ACTIVE
                )
                .setParameter("notifiedStatus",
                        ReservationStatus.NOTIFIED
                )
                .setParameter("fulfilledStatus",
                        ReservationStatus.FULFILLED
                )
                .setParameter("cancelledStatus",
                        ReservationStatus.CANCELLED
                )
                .setParameter("expiredStatus",
                        ReservationStatus.CANCELLED
                )
                .setMaxResults(limit)
                .getResultList();
    }
}
