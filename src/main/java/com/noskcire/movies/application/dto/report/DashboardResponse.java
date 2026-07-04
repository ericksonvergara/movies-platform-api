package com.noskcire.movies.application.dto.report;

import com.noskcire.movies.domain.model.Movie;

public record DashboardResponse(

        MovieDashboardResponse movies,
        PersonDashboardResponse people,
        RentalDashboardResponse rentals,
        ReservationDashboardResponse reservations,
        LateFeeDashboardResponse lateFees,
        RevenueDashboardResponse revenue
) {
}
