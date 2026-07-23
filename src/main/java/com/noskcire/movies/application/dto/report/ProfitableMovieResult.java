package com.noskcire.movies.application.dto.report;

import com.noskcire.movies.domain.enums.MovieProfitabilitySort;

import java.util.List;

public record ProfitableMovieResult(
        Integer limit,
        MovieProfitabilitySort sortBy,
        Integer totalResult,
        List<ProfitableMovieResponse> data

) {
}
