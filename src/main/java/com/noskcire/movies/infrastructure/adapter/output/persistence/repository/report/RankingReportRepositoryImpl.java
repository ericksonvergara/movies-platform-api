package com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report;

import com.noskcire.movies.domain.enums.MovieRankingSort;
import com.noskcire.movies.domain.enums.RentalStatus;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.MovieRankingProjection;
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
}
