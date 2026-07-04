package com.noskcire.movies.application.dto.report;

public record ReservationDashboardResponse(

        long active,
        long notified,
        long expired
) {
}
