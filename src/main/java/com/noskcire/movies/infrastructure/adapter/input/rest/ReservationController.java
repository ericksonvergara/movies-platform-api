package com.noskcire.movies.infrastructure.adapter.input.rest;

import com.noskcire.movies.application.dto.reservation.CreateReservationRequest;
import com.noskcire.movies.application.dto.reservation.ReservationResponse;
import com.noskcire.movies.application.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Crear una nueva reserva", description = "Registra una nueva reserva de película")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la reserva inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENT', 'EMPLOYEE', 'ADMIN')")
    public ReservationResponse createReservation(
            @Valid
            @RequestBody
            CreateReservationRequest request
    ) {
        return reservationService.createReservation(request);
    }

    @Operation(summary = "Obtener todas las reservas", description = "Devuelve el listado completo de todas las reservas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de reservas obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public List<ReservationResponse> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @Operation(summary = "Obtener reserva por ID", description = "Devuelve los detalles de una reserva específica por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public ReservationResponse getReservationById(
            @PathVariable Long id
    ) {
        return reservationService.getReservationById(id);
    }

    @Operation(summary = "Obtener reservas activas", description = "Devuelve el listado de reservas que aún están vigentes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de reservas activas obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public List<ReservationResponse> getActiveReservations() {
        return reservationService.getActiveReservations();
    }

    @Operation(summary = "Obtener reservas por cliente", description = "Devuelve el listado de reservas asociadas a un cliente específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de reservas del cliente obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/client/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public List<ReservationResponse> getReservationByClient(
            @PathVariable Long id
    ) {
        return reservationService.getReservationByClient(id);
    }

    @Operation(summary = "Obtener mis reservas", description = "Devuelve el listado de reservas del usuario autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de mis reservas obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @GetMapping("/me")
    public List<ReservationResponse> getMyReservations() {
        return reservationService.getMyReservations();
    }

    @Operation(summary = "Obtener reservas por película", description = "Devuelve el listado de reservas asociadas a una película específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de reservas de la película obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Película no encontrada")
    })
    @GetMapping("/movie/{movieId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public List<ReservationResponse> getReservationsByMovie(
            @PathVariable Long movieId
    ) {
        return reservationService.getReservationsByMovie(movieId);
    }

    @Operation(summary = "Cancelar reserva", description = "Cancela una reserva existente por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva cancelada correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public ReservationResponse cancelReservation(
            @PathVariable Long id
    ) throws AccessDeniedException {
        return reservationService.cancelReservation(id);
    }

}
