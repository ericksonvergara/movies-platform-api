package com.noskcire.movies.application.dto.report;

import com.noskcire.movies.domain.enums.ClientRankingSort;

import java.util.List;

public record ClientRankingResult(
        Integer limit,
        ClientRankingSort sortBy,
        Integer totalResults,
        List<ClientRankingResponse> data
) {
}
