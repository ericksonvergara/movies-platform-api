package com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report;

import com.noskcire.movies.domain.enums.LateFeeStatus;
import com.noskcire.movies.domain.enums.PersonType;
import com.noskcire.movies.domain.enums.RentalStatus;
import com.noskcire.movies.domain.enums.ReservationStatus;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.report.projection.RevenueSummary;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
@RequiredArgsConstructor
public class DashboardReportRepositoryImpl
        implements DashboardReportRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public long countMovies() {
        return entityManager.createQuery("""
                        SELECT COUNT(m)
                        FROM Movie m
                        """, Long.class)
                .getSingleResult();
    }

    @Override
    public long countAvailableMovies() {
        return entityManager.createQuery("""
                        SELECT COUNT(m)
                        FROM Movie m
                        WHERE m.stock > 0
                        """, Long.class)
                .getSingleResult();
    }

    @Override
    public long countRentedMovies() {
        return entityManager.createQuery("""
                        SELECT COUNT(m)
                        FROM Movie m
                        WHERE m.stock = 0
                        """, Long.class)
                .getSingleResult();
    }

    @Override
    public long countClients() {
        return entityManager.createQuery("""
                        SELECT COUNT(p)
                        FROM Person p
                        WHERE p.type = :type
                        """, Long.class)
                .setParameter("type", PersonType.CLIENT)
                .getSingleResult();
    }

    @Override
    public long countEmployees() {
        return entityManager.createQuery("""
                        SELECT COUNT(p)
                        FROM Person p
                        WHERE p.type = :type
                        """, Long.class)
                .setParameter("type", PersonType.EMPLOYEE)
                .getSingleResult();
    }

    @Override
    public long countActiveRentals() {
        return entityManager.createQuery("""
                        SELECT COUNT(r)
                        FROM Rental r
                        WHERE r.status = :status
                        """, Long.class)
                .setParameter("status", RentalStatus.ACTIVE)
                .getSingleResult();
    }

    @Override
    public long countReturnedRentals() {
        return entityManager.createQuery("""
                        SELECT COUNT(r)
                        FROM Rental r
                        WHERE r.status = :status
                        """, Long.class)
                .setParameter("status", RentalStatus.RETURNED)
                .getSingleResult();
    }

    @Override
    public long countActiveReservations() {
        return entityManager.createQuery("""
                        SELECT COUNT(res)
                        FROM Reservation res
                        WHERE res.status = :status
                        """, Long.class)
                .setParameter("status", ReservationStatus.ACTIVE)
                .getSingleResult();
    }

    @Override
    public long countNotifiedReservations() {
        return entityManager.createQuery("""
                        SELECT COUNT(res)
                        FROM Reservation res
                        WHERE res.status = :status
                        """, Long.class)
                .setParameter("status", ReservationStatus.NOTIFIED)
                .getSingleResult();
    }

    @Override
    public long countExpiredReservations() {
        return entityManager.createQuery("""
                        SELECT COUNT(res)
                        FROM Reservation res
                        WHERE res.status = :status
                        """, Long.class)
                .setParameter("status", ReservationStatus.EXPIRED)
                .getSingleResult();
    }

    @Override
    public long countActiveLateFees() {
        return entityManager.createQuery("""
                        SELECT COUNT(lf)
                        FROM LateFee lf
                        WHERE lf.status = :status
                        """, Long.class)
                .setParameter("status", LateFeeStatus.ACTIVE)
                .getSingleResult();
    }

    @Override
    public long countPendingLateFees() {
        return entityManager.createQuery("""
                        SELECT COUNT(lf)
                        FROM LateFee lf
                        WHERE lf.status = :status
                        """, Long.class)
                .setParameter("status", LateFeeStatus.PENDING)
                .getSingleResult();
    }

    @Override
    public long countPaidLateFees() {
        return entityManager.createQuery("""
                        SELECT COUNT(lf)
                        FROM LateFee lf
                        WHERE lf.status = :status
                        """, Long.class)
                .setParameter("status", LateFeeStatus.PAID)
                .getSingleResult();
    }

    @Override
    public RevenueSummary getRevenueSummary() {
        BigDecimal rentals =
                entityManager.createQuery("""
                                SELECT COALESCE(SUM(r.total), 0)
                                FROM Rental r
                                WHERE r.status = :status
                                """, BigDecimal.class)
                        .setParameter("status", RentalStatus.RETURNED)
                        .getSingleResult();

        BigDecimal lateFees = entityManager.createQuery("""
                        SELECT COALESCE(SUM(lf.totalAmount), 0)
                        FROM LateFee lf
                        WHERE lf.status = :status
                        """, BigDecimal.class)
                .setParameter("status", LateFeeStatus.PAID)
                .getSingleResult();

        return new RevenueSummary(rentals, lateFees);
    }
}
