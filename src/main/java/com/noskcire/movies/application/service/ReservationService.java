package com.noskcire.movies.application.service;

import com.noskcire.movies.application.dto.reservation.CreateReservationRequest;
import com.noskcire.movies.application.dto.reservation.ReservationResponse;
import com.noskcire.movies.domain.enums.PersonType;
import com.noskcire.movies.domain.enums.RentalStatus;
import com.noskcire.movies.domain.enums.ReservationStatus;
import com.noskcire.movies.domain.exception.BadRequestException;
import com.noskcire.movies.domain.exception.ResourceNotFoundException;
import com.noskcire.movies.domain.model.Movie;
import com.noskcire.movies.domain.model.Person;
import com.noskcire.movies.domain.model.Reservation;
import com.noskcire.movies.domain.model.User;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MovieRepository movieRepository;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;

    //return mapToResponse(Reservation);

    private ReservationResponse mapToResponse(
            Reservation reservation
    ) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getMovie().getId(),
                reservation.getMovie().getTitle(),
                reservation.getClient().getId(),
                reservation.getClient().getNames()
                        + " "
                        + reservation.getClient().getLastNames(),
                reservation.getReservationDate(),
                reservation.getExpirationDate(),
                reservation.getStatus(),
                reservation.isNotificationSent(),
                reservation.getFulfilledAt()
        );
    }

    private User getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return userRepository
                .findByUsername(
                        authentication.getName()
                ).orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Usuario no encontrado"
                        )
                );
    }

    private Person resolveClient(
            CreateReservationRequest request
    ) {
        User authenticatedUser =
                getAuthenticatedUser();

        String role =
                authenticatedUser
                        .getRole()
                        .getName()
                        .toUpperCase();

        switch (role) {

            case "CLIENT" -> {

                Person client = authenticatedUser.getPerson();


                if (client.getType() != PersonType.CLIENT) {
                    throw new BadRequestException(
                            "El usuario autenticado no es un cliente."
                    );
                }
                return client;
            }

            case "EMPLOYEE", "ADMIN" -> {
                if (request.clientId() == null) {
                    throw new BadRequestException(
                            "Debe indicar el cliente de la reserva."
                    );
                }

                Person client =
                        personRepository
                                .findById(request.clientId())
                                .orElseThrow(
                                        () -> new ResourceNotFoundException(
                                                "Cliente no encontrado."
                                        )
                                );

                if (client.getType() != PersonType.CLIENT) {
                    throw new BadRequestException(
                            "La persona seleccionada no es un cliente."
                    );
                }

                return client;
            }

            default -> throw new BadRequestException(
                    "No tiene permisos para crear reservas."
            );
        }
    }


    public ReservationResponse createReservation(
            CreateReservationRequest request
    ) {


        Movie movie =
                movieRepository
                        .findById(
                                request.movieId()
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Película no encontrada"
                                )
                        );

        if (!movie.isEnabled()) {
            throw new BadRequestException(
                    "La película no se encuentra disponible."
            );
        }

        if (movie.getStock() > 0) {
            throw new BadRequestException(
                    "La película se encuentra disponible para alquiler. No es necesario crear una reserva."
            );
        }

        Person client =
                resolveClient(request);

        if (reservationRepository.existsByClientAndMovieAndStatus(
                client,
                movie,
                ReservationStatus.ACTIVE
        )) {
            throw new BadRequestException(
                    "El cliente ya tiene una reserva activa para esta película."
            );
        }

        if (reservationRepository.countByClientAndStatus(
                client,
                ReservationStatus.ACTIVE
        ) >= 5
        ) {
            throw new BadRequestException(
                    "El cliente ya tiene 5 reservas activas. No puede crear más reservas."
            );
        }

        if (rentalRepository.existsActiveRentalByClientAndMovie(
                client,
                movie,
                RentalStatus.ACTIVE
        )) {
            throw new BadRequestException(
                    "El cliente ya tiene esta película alquilada."
            );
        }

        Reservation reservation =
                reservationRepository.save(
                        Reservation.builder()
                                .movie(movie)
                                .client(client)
                                .status(ReservationStatus.ACTIVE)
                                .notificationSent(false)
                                .build()
                );

        return mapToResponse(reservation);
    }

    public List<ReservationResponse> getAllReservations() {
        List<Reservation> reservations =
                reservationRepository.findAll();

        return reservations.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ReservationResponse getReservationById(
            Long id
    ) {
        Reservation reservation =
                reservationRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Reserva no encontrada."
                                )
                        );

        return mapToResponse(reservation);
    }

    public List<ReservationResponse> getActiveReservations() {
        List<Reservation> reservations =
                reservationRepository.
                        findByStatus(
                                ReservationStatus.ACTIVE
                        );

        return reservations.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ReservationResponse> getMyReservations() {

        Person client = getAuthenticatedUser().getPerson();

        List<Reservation> reservations =
                reservationRepository.findByClient(client);

        return reservations.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ReservationResponse> getReservationByClient(
            Long clientid
    ) {
        Person client =
                personRepository.findById(clientid)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Cliente no encontrado."
                                )
                        );

        if (client.getType() != PersonType.CLIENT) {
            throw new BadRequestException(
                    "La persona seleccionada no es un cliente."
            );
        }

        return reservationRepository
                .findByClient(client)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ReservationResponse> getReservationsByMovie(
            Long movieId
    ) {
        Movie movie =
                movieRepository.findById(movieId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Película no encontrada."
                                )
                        );

        return reservationRepository
                .findFirstByMovieAndStatusOrderByReservationDateAsc(
                        movie,
                        ReservationStatus.ACTIVE
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ReservationResponse cancelReservation(
            Long reservationId
    ) throws AccessDeniedException {
        Reservation reservation =
                reservationRepository.findById(reservationId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Reserva no encontrada."
                                )
                        );

        User authenticatedUser = getAuthenticatedUser();

        String role =
                authenticatedUser.getRole()
                        .getName()
                        .toUpperCase();

        if ("CLIENT".equals(role)
                && !reservation.getClient().getId().equals(
                authenticatedUser.getPerson().getId()
        )) {
            throw new AccessDeniedException(
                    "No tiene permisos para cancelar esta reserva."
            );
        }

        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new BadRequestException(
                    "La reserva ya no se encuentra activa."
            );
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
        return mapToResponse(reservation);
    }

    public void activateNextReservation(
            Movie movie
    ) {


        if (!movie.isEnabled()) {
            return;
        }

        if (movie.getStock() <= 0) {
            return;
        }

        Reservation reservation =
                reservationRepository
                        .findFirstByMovieAndStatusOrderByReservationDateAsc(
                                movie,
                                ReservationStatus.ACTIVE
                        )
                        .orElse(null);

        System.out.println(
                reservationRepository
                        .findFirstByMovieAndStatusOrderByReservationDateAsc(
                                movie,
                                ReservationStatus.ACTIVE
                        )
        );

        if (reservation == null) {
            return;
        }

        reservation.setStatus(
                ReservationStatus.NOTIFIED
        );

        reservation.setNotificationSent(true);

        reservation.setExpirationDate(
                LocalDateTime.now().plusHours(48)
        );


        reservationRepository.save(reservation);
    }

    private void expireReservation(
            Reservation reservation
    ) {

        reservation.setStatus(ReservationStatus.EXPIRED);
        reservation.setNotificationSent(false);
        reservationRepository.save(reservation);
        activateNextReservation(reservation.getMovie());
    }

    @Scheduled(fixedRate = 60000)
    public void expireReservations() {


        reservationRepository
                .findByStatusAndExpirationDateBefore(
                        ReservationStatus.NOTIFIED,
                        LocalDateTime.now()
                )
                .forEach(this::expireReservation);


    }

    public void fulfillReservation(
            Person client,
            Movie movie
    ) {

        Reservation reservation =
                reservationRepository
                        .findFirstByClientAndMovieAndStatus(
                                client,
                                movie,
                                ReservationStatus.NOTIFIED
                        )
                        .orElse(null);

        if (reservation == null) {
            return;
        }

        reservation.setStatus(ReservationStatus.FULFILLED);
        reservation.setFulfilledAt(LocalDateTime.now());
        reservation.setNotificationSent(false);
        reservationRepository.save(reservation);
    }
}

