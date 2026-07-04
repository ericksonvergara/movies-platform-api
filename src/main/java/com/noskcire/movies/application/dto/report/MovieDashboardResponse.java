package com.noskcire.movies.application.dto.report;

public record MovieDashboardResponse(

        long total,
        long available,
        long rented
) {
}
