package com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report;

import com.noskcire.movies.domain.enums.*;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.*;

import java.util.List;

public interface RankingReportRepository {

    List<MovieRankingProjection> getMovieRanking(
            Integer limit,
            MovieRankingSort sort
    );

    List<ClientRankingProjection> getClientRanking(
            Integer limit,
            ClientRankingSort sort
    );

    List<LateFeeRankingProjection> getLateFeeRanking(
            Integer limit,
            LateFeeRankingSort sort
    );

    List<ReservationRankingProjection> getReservationRanking(
            Integer limit,
            ReservationRankingSort sort
    );

//    List<MovieMostRankingProjection> getMovieMostRanking(
//            Integer limit,
//            MovieMostRankingSort sort
//    );
}
