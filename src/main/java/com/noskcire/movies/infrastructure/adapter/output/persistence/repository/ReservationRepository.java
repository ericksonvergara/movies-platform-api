package com.noskcire.movies.infrastructure.adapter.output.persistence.repository;

import com.noskcire.movies.domain.enums.ReservationStatus;
import com.noskcire.movies.domain.model.Movie;
import com.noskcire.movies.domain.model.Person;
import com.noskcire.movies.domain.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByClientAndMovieAndStatus(
            Person client,
            Movie movie,
            ReservationStatus status
    );

    List<Reservation> findByStatus(ReservationStatus status);

    List<Reservation> findByClient(Person client);

    List<Reservation> findByMovie(Movie movie);

    Optional<Reservation> findFirstByMovieAndStatusOrderByReservationDateAsc(
            Movie movie,
            ReservationStatus status
    );

    long countByClientAndStatus(
            Person client,
            ReservationStatus status
    );

    List<Reservation> findByStatusAndExpirationDateBefore(
            ReservationStatus status,
            LocalDateTime date
    );

    Optional<Reservation> findFirstByClientAndMovieAndStatus(
            Person client,
            Movie movie,
            ReservationStatus status
    );
}
