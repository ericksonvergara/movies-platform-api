package com.noskcire.movies.infrastructure.adapter.input.rest;

import com.noskcire.movies.application.dto.lateFee.LateFeeResponse;
import com.noskcire.movies.application.dto.lateFee.PayLateFeeRequest;
import com.noskcire.movies.application.service.LateFeeService;
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

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public List<LateFeeResponse> getAllLateFees(){
        return lateFeeService.getAllLateFees();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public LateFeeResponse getLateFeeById(
            @PathVariable Long id
    ){
        return lateFeeService.getLateFeeById(id);
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public List<LateFeeResponse> getActiveLateFees(){
        return lateFeeService.getActiveLateFees();
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public List<LateFeeResponse> getPendingLateFees(){
        return lateFeeService.getPendingLateFees();
    }

    @GetMapping("/paid")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public List<LateFeeResponse> getPaidLateFees(){
        return lateFeeService.getPaidLateFees();
    }

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
