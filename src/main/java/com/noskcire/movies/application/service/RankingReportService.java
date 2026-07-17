package com.noskcire.movies.application.service;

import com.noskcire.movies.application.dto.report.ClientRankingResponse;
import com.noskcire.movies.application.dto.report.ClientRankingResult;
import com.noskcire.movies.application.dto.report.MovieRankingResponse;

import com.noskcire.movies.application.dto.report.MovieRankingResult;
import com.noskcire.movies.domain.enums.ClientRankingSort;
import com.noskcire.movies.domain.enums.MovieRankingSort;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.RankingReportRepository;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.ClientRankingProjection;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.MovieRankingProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankingReportService {

    private final RankingReportRepository rankingReportRepository;


    public MovieRankingResult getMovieRanking(
            Integer limit,
            MovieRankingSort sort
    ) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }

        List<MovieRankingProjection> rankings =
                rankingReportRepository.getMovieRanking(
                        limit,
                        sort
                );

        List<MovieRankingResponse> data =
                rankings.stream()
                        .map(ranking -> new MovieRankingResponse(
                                ranking.movieId(),
                                ranking.title(),
                                ranking.timesRented(),
                                ranking.unitsRented(),
                                ranking.averageUnits(),
                                ranking.revenue()
                        ))
                        .toList();

        return new MovieRankingResult(
                limit,
                sort,
                data.size(),
                data
        );
    }

    public ClientRankingResult getClientRanking(
            Integer limit,
            ClientRankingSort sort
    ) {
//        if (limit == null || limit <= 0) {
//            limit = 10;
//        }

        List<ClientRankingProjection> rankings =
                rankingReportRepository.getClientRanking(
                        limit,
                        sort
                );

        List<ClientRankingResponse> data =
                rankings.stream()
                        .map(ranking -> new ClientRankingResponse(
                                ranking.clientId(),
                                ranking.name(),
                                ranking.email(),
                                ranking.totalRentals(),
                                ranking.totalSpent()
                        ))
                        .toList();

        return new ClientRankingResult(
                limit,
                sort,
                data.size(),
                data
        );
    }
}
