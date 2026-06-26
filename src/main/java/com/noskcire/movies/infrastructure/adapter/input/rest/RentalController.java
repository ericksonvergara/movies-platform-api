package com.noskcire.movies.infrastructure.adapter.input.rest;

import com.noskcire.movies.application.dto.rental.CreateRentalRequest;
import com.noskcire.movies.application.dto.rental.RentalResponse;
import com.noskcire.movies.application.service.RentalService;
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

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public RentalResponse createRental(
            @Valid
            @RequestBody
            CreateRentalRequest createRentalRequest
    ) {
        return rentalService.createRental(createRentalRequest);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public RentalResponse getRentalById(
            @PathVariable
            Long id
    ){
        return rentalService.getRentalById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public List<RentalResponse> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public List<RentalResponse> getActiveRentals() {
        return rentalService.getRentalActive();
    }

    @GetMapping("/returned")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public List<RentalResponse> getReturnedRentals() {
        return rentalService.getRentalReturned();
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public List<RentalResponse> getRentalsByClientId(
            @PathVariable Long clientId
    ) {
        return rentalService.getRentalsByClientId(clientId);
    }

    @PostMapping("/{id}/return")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public RentalResponse returnRental(
            @PathVariable Long id
    ) {
        return  rentalService.returnRental(id);
    }

    @GetMapping("/overdure")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public List<RentalResponse> getOverdueRentals() {
        return rentalService.getOverdueRentals();
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public List<RentalResponse> getRentalsByEmployeeId(
            @PathVariable Long employeeId
    ) {
        return rentalService.getRentalsByEmployeeId(employeeId);
    }
}
