package com.noskcire.movies.infrastructure.adapter.input.rest;

import com.noskcire.movies.application.dto.lateFee.LateFeeResponse;
import com.noskcire.movies.application.dto.lateFee.PayLateFeeRequest;
import com.noskcire.movies.application.service.LateFeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/late-fees")
@RequiredArgsConstructor
public class LateFeeController {

    private final LateFeeService lateFeeService;

    @Operation(summary = "Obtener todas las multas", description = "Devuelve el listado completo de todas las multas por devolución tardía")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de multas obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public List<LateFeeResponse> getAllLateFees(){
        return lateFeeService.getAllLateFees();
    }

    @Operation(summary = "Obtener multa por ID", description = "Devuelve los detalles de una multa específica por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Multa encontrada"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Multa no encontrada")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public LateFeeResponse getLateFeeById(
            @PathVariable Long id
    ){
        return lateFeeService.getLateFeeById(id);
    }

    @Operation(summary = "Obtener multas activas", description = "Devuelve el listado de multas que aún no han sido pagadas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de multas activas obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public List<LateFeeResponse> getActiveLateFees(){
        return lateFeeService.getActiveLateFees();
    }

    @Operation(summary = "Obtener multas pendientes", description = "Devuelve el listado de multas pendientes de pago")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de multas pendientes obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public List<LateFeeResponse> getPendingLateFees(){
        return lateFeeService.getPendingLateFees();
    }

    @Operation(summary = "Obtener multas pagadas", description = "Devuelve el listado de multas que ya han sido pagadas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de multas pagadas obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/paid")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public List<LateFeeResponse> getPaidLateFees(){
        return lateFeeService.getPaidLateFees();
    }

    @Operation(summary = "Pagar multa", description = "Registra el pago de una multa específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Multa pagada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de pago inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Multa no encontrada")
    })
    @PatchMapping("/{id}/pay")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public LateFeeResponse payLateFee(
            @PathVariable Long id,
            @Valid
            @RequestBody PayLateFeeRequest request
    ) {
        return lateFeeService.payLateFee(id, request);
    }
}
