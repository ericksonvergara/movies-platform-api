package com.noskcire.movies.infrastructure.adapter.input.rest;

import com.noskcire.movies.application.dto.rental.CreateRentalRequest;
import com.noskcire.movies.application.dto.rental.RentalResponse;
import com.noskcire.movies.application.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{id}/return")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public RentalResponse returnRental(
            @PathVariable Long id
    ) {
        return  rentalService.returnRental(id);
    }

}
