package com.noskcire.movies.infrastructure.adapter.input.rest;

import com.noskcire.movies.application.dto.reservation.CreateReservationRequest;
import com.noskcire.movies.application.dto.reservation.ReservationResponse;
import com.noskcire.movies.application.service.ReservationService;
import com.noskcire.movies.domain.model.Reservation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENT', 'EMPLOYEE', 'ADMIN')")
    public ReservationResponse createReservation(
            @Valid
            @RequestBody
            CreateReservationRequest request
    ) {
        return reservationService.createReservation(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public List<ReservationResponse> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public ReservationResponse getReservationById(
            @PathVariable Long id
    ) {
        return reservationService.getReservationById(id);
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public List<ReservationResponse> getActiveReservations() {
        return reservationService.getActiveReservations();
    }

    @GetMapping("/client/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public List<ReservationResponse> getReservationByClient(
            @PathVariable Long id
    ) {
        return reservationService.getReservationByClient(id);
    }

    @GetMapping("/me")
    public List<ReservationResponse> getMyReservations() {
        return reservationService.getMyReservations();
    }

    @GetMapping("/movie/{movieId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public List<ReservationResponse> getReservationsByMovie(
            @PathVariable Long movieId
    ) {
        return reservationService.getReservationsByMovie(movieId);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public ReservationResponse cancelReservation(
            @PathVariable Long id
    ) throws AccessDeniedException {
        return reservationService.cancelReservation(id);
    }

}
