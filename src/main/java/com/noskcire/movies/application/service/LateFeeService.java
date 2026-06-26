package com.noskcire.movies.application.service;

import com.noskcire.movies.application.dto.lateFee.LateFeeResponse;
import com.noskcire.movies.domain.enums.LateFeeStatus;
import com.noskcire.movies.domain.exception.ResourceNotFoundException;
import com.noskcire.movies.domain.model.LateFee;
import com.noskcire.movies.domain.model.Rental;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.LateFeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LateFeeService {

    @Value("${application.late-fee.daily-amount}")
    private BigDecimal dailyLateFee;

    private final LateFeeRepository lateFeeRepository;

    private LateFeeResponse mapToResponse(
            LateFee lateFee
    ) {
        return new LateFeeResponse(
                lateFee.getId(),
                lateFee.getRental().getId(),
                lateFee.getRental().getClient().getId(),
                lateFee.getRental().getClient().getNames()
                        + " "
                        + lateFee.getRental().getClient().getLastNames(),
                lateFee.getDaysLate(),
                lateFee.getDailyAmount(),
                lateFee.getTotalAmount(),
                lateFee.getStatus()
        );
    }

    public List<LateFeeResponse> getAllLateFees() {
        List<LateFee> lateFees
                = lateFeeRepository.findAll();

        return lateFees.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public LateFeeResponse getLateFeeById(
            Long id
    ){
        LateFee lateFee =
                lateFeeRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Multa no encontrada."
                                )
                        );
        return mapToResponse(lateFee);
    }

    public List<LateFeeResponse> getPendingLateFees(){
        List<LateFee> lateFees =
                lateFeeRepository.findByStatus(
                        LateFeeStatus.PENDING
                );
        
        return lateFees.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<LateFeeResponse> getPaidLateFees(){

        List<LateFee> lateFees =
                lateFeeRepository.findByStatus(
                        LateFeeStatus.PAID
                );

        return lateFees.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void generateLateFee(
            Rental rental
    ){
        
    }
}
