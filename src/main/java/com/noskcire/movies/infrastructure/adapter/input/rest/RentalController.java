package com.noskcire.movies.infrastructure.adapter.input.rest;

import com.noskcire.movies.application.dto.rental.CreateRentalRequest;
import com.noskcire.movies.application.dto.rental.RentalResponse;
import com.noskcire.movies.application.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;

    @Operation(summary = "Crear un nuevo alquiler", description = "Registra un nuevo alquiler de película")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alquiler creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos del alquiler inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public RentalResponse createRental(
            @Valid
            @RequestBody
            CreateRentalRequest createRentalRequest
    ) {
        return rentalService.createRental(createRentalRequest);
    }

    @Operation(summary = "Obtener alquiler por ID", description = "Devuelve los detalles de un alquiler específico por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alquiler encontrado"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Alquiler no encontrado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public RentalResponse getRentalById(
            @PathVariable
            Long id
    ){
        return rentalService.getRentalById(id);
    }

    @Operation(summary = "Obtener todos los alquileres", description = "Devuelve el listado completo de todos los alquileres")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de alquileres obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public List<RentalResponse> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @Operation(summary = "Obtener alquileres activos", description = "Devuelve el listado de alquileres que aún no han sido devueltos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de alquileres activos obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public List<RentalResponse> getActiveRentals() {
        return rentalService.getRentalActive();
    }

    @Operation(summary = "Obtener alquileres devueltos", description = "Devuelve el listado de alquileres que ya han sido devueltos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de alquileres devueltos obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/returned")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public List<RentalResponse> getReturnedRentals() {
        return rentalService.getRentalReturned();
    }

    @Operation(summary = "Obtener alquileres por cliente", description = "Devuelve el listado de alquileres asociados a un cliente específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de alquileres del cliente obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public List<RentalResponse> getRentalsByClientId(
            @PathVariable Long clientId
    ) {
        return rentalService.getRentalsByClientId(clientId);
    }

    @Operation(summary = "Devolver película alquilada", description = "Registra la devolución de una película alquilada")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Devolución registrada correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Alquiler no encontrado")
    })
    @PostMapping("/{id}/return")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public RentalResponse returnRental(
            @PathVariable Long id
    ) {
        return  rentalService.returnRental(id);
    }

    @Operation(summary = "Obtener alquileres vencidos", description = "Devuelve el listado de alquileres que están fuera de plazo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de alquileres vencidos obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/overdure")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public List<RentalResponse> getOverdueRentals() {
        return rentalService.getOverdueRentals();
    }

    @Operation(summary = "Obtener alquileres por empleado", description = "Devuelve el listado de alquileres registrados por un empleado específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de alquileres del empleado obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public List<RentalResponse> getRentalsByEmployeeId(
            @PathVariable Long employeeId
    ) {
        return rentalService.getRentalsByEmployeeId(employeeId);
    }
}
