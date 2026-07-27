package com.noskcire.movies.infrastructure.adapter.output.persistence.repository;

import com.noskcire.movies.domain.enums.RentalStatus;
import com.noskcire.movies.domain.model.Movie;
import com.noskcire.movies.domain.model.Person;
import com.noskcire.movies.domain.model.Rental;
import com.noskcire.movies.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByClient(Person client);

    List<Rental> findByEmployee(User employee);

    List<Rental> findByStatus(RentalStatus status);

    List<Rental> findByStatusAndExpectedReturnDateBefore(
            RentalStatus status,
            LocalDate date
    );

    @Query("""
                SELECT EXISTS (
                    SELECT rd
                    FROM RentalDetail rd
                    WHERE rd.rental.client = :client
                      AND rd.movie = :movie
                      AND rd.rental.status = :status
                )
            """)
    boolean existsActiveRentalByClientAndMovie(
            @Param("client") Person client,
            @Param("movie") Movie movie,
            @Param("status") RentalStatus status
    );

}
