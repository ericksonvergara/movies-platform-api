package com.noskcire.movies.application.service;

import com.noskcire.movies.application.dto.report.*;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.DashboardReportRepository;

import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.RevenueSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final DashboardReportRepository dashboardReportRepository;

    public DashboardResponse dashboard(){
        MovieDashboardResponse movies =
                new MovieDashboardResponse(
                        dashboardReportRepository.countMovies(),
                        dashboardReportRepository.countAvailableMovies(),
                        dashboardReportRepository.countRentedMovies()
                );

        PersonDashboardResponse people =
                new PersonDashboardResponse(
                        dashboardReportRepository.countClients(),
                        dashboardReportRepository.countEmployees()
                );

        RentalDashboardResponse rentals =
                new RentalDashboardResponse(
                        dashboardReportRepository.countActiveRentals(),
                        dashboardReportRepository.countReturnedRentals()
                );

        ReservationDashboardResponse reservations =
                new ReservationDashboardResponse(
                        dashboardReportRepository.countActiveReservations(),
                        dashboardReportRepository.countNotifiedReservations(),
                        dashboardReportRepository.countExpiredReservations()
                );

        LateFeeDashboardResponse lateFees =
                new LateFeeDashboardResponse(
                        dashboardReportRepository.countActiveLateFees(),
                        dashboardReportRepository.countPendingLateFees(),
                        dashboardReportRepository.countPaidLateFees()
                );

        RevenueSummary summary =
                dashboardReportRepository.getRevenueSummary();

        RevenueDashboardResponse revenue =
                new RevenueDashboardResponse(
                        summary.rentals(),
                        summary.lateFees(),
                        summary.rentals()
                                .add(summary.lateFees())
                );

        return new DashboardResponse(
                movies,
                people,
                rentals,
                reservations,
                lateFees,
                revenue);
    }

}
