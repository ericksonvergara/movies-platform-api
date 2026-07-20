package com.noskcire.movies.application.dto.report;

import com.noskcire.movies.domain.enums.ReservationRankingSort;

import java.util.List;

public record ReservationRankingResult(
        Integer limit,
        ReservationRankingSort sortBy,
        Integer totalResults,
        List<ReservationRankingResponse> data
) {
}
