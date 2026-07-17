package com.noskcire.movies.application.dto.report;

import com.noskcire.movies.domain.enums.MovieRankingSort;

import java.util.List;

public record MovieRankingResult(

        Integer limit,

        MovieRankingSort sortBy,

        Integer totalResults,

        List<MovieRankingResponse> data
) {
}
