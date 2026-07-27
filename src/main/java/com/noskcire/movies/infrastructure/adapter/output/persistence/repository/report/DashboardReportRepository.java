package com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report;

import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.RevenueSummary;

public interface DashboardReportRepository {

    // Movies
    long countMovies();
    long countAvailableMovies();
    long countRentedMovies();

//    // People
    long countClients();
    long countEmployees();

//    // Rentals
    long countActiveRentals();
    long countReturnedRentals();

    // Reservations
    long countActiveReservations();
    long countNotifiedReservations();
    long countExpiredReservations();

    // Late Fees
    long countActiveLateFees();
    long countPendingLateFees();
    long countPaidLateFees();

    // Revenue
    RevenueSummary getRevenueSummary();
}
