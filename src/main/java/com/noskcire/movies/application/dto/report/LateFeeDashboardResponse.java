package com.noskcire.movies.application.dto.report;

public record LateFeeDashboardResponse(

        long active,
        long pending,
        long paid
) {
}
