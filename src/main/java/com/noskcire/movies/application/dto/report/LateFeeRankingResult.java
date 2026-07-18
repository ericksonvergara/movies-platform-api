package com.noskcire.movies.application.dto.report;

import com.noskcire.movies.domain.enums.ClientRankingSort;
import com.noskcire.movies.domain.enums.LateFeeRankingSort;

import java.util.List;

public record LateFeeRankingResult(
        Integer limit,
        LateFeeRankingSort sortBy,
        Integer totalResults,
        List<LateFeeRankingResponse> data
) {
}
