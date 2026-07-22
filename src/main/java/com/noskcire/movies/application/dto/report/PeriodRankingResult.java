package com.noskcire.movies.application.dto.report;

import com.noskcire.movies.domain.enums.PeriodRankingSort;

import java.util.List;

public record PeriodRankingResult(
        Integer limit,
        PeriodRankingSort sortBy,
        Integer totalResults,
        List<PeriodRankingResponse> data
) {
}
