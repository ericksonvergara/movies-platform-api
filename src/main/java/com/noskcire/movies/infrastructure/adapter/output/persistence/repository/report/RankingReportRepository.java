package com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report;

import com.noskcire.movies.domain.enums.MovieRankingSort;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.MovieRankingProjection;

import java.util.List;

public interface RankingReportRepository {

    List<MovieRankingProjection> getMovieRanking(
            Integer limit,
            MovieRankingSort sort
    );
}
