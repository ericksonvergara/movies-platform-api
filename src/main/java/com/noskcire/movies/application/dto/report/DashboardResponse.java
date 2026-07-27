package com.noskcire.movies.application.dto.report;

public record DashboardResponse(

        MovieDashboardResponse movies,
        PersonDashboardResponse people,
        RentalDashboardResponse rentals,
        ReservationDashboardResponse reservations,
        LateFeeDashboardResponse lateFees,
        RevenueDashboardResponse revenue
) {
}
