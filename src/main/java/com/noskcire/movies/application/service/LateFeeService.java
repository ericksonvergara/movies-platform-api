package com.noskcire.movies.application.service;

import com.noskcire.movies.application.dto.lateFee.LateFeeResponse;
import com.noskcire.movies.application.dto.lateFee.PayLateFeeRequest;
import com.noskcire.movies.domain.enums.LateFeeStatus;
import com.noskcire.movies.domain.enums.RentalStatus;
import com.noskcire.movies.domain.exception.BadRequestException;
import com.noskcire.movies.domain.exception.ResourceNotFoundException;
import com.noskcire.movies.domain.model.LateFee;
import com.noskcire.movies.domain.model.Rental;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.LateFeeRepository;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.RentalRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LateFeeService {

    @Value("${application.late-fee.daily-amount}")
    private BigDecimal dailyLateFee;

    private final LateFeeRepository lateFeeRepository;
    private final RentalRepository rentalRepository;

    private LateFeeResponse mapToResponse(
            LateFee lateFee
    ) {

        Long daysLate = lateFee.getDaysLate();
        BigDecimal totalAmount = lateFee.getTotalAmount();

        if (lateFee.getStatus() == LateFeeStatus.ACTIVE){

            daysLate = Math.max(0,
                    ChronoUnit.DAYS.between(
                        lateFee.getRental().getExpectedReturnDate(),
                        LocalDate.now()
                    )
            );

            totalAmount = lateFee.getDailyAmount().multiply(
                    BigDecimal.valueOf(daysLate)
            );
        }

        return new LateFeeResponse(
                lateFee.getId(),
                lateFee.getRental().getId(),
                lateFee.getRental().getClient().getId(),
                lateFee.getRental().getClient().getNames()
                        + " "
                        + lateFee.getRental().getClient().getLastNames(),
                daysLate,
                lateFee.getDailyAmount(),
                totalAmount,
                lateFee.getStatus(),
                lateFee.getPaymentDate(),
                lateFee.getPaymentMethod(),
                lateFee.getObservations()
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

    public List<LateFeeResponse> getActiveLateFees(){
        List<LateFee> lateFees =
                lateFeeRepository.findByStatus(
                        LateFeeStatus.ACTIVE
                );

        return lateFees.stream()
                .map(this::mapToResponse)
                .toList();
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

//    @Scheduled(cron = "0 0 0 * * *")
    @Scheduled(fixedRate = 60000)
    public void generateLateFees(){

        List<Rental> rentals =
                rentalRepository.findByStatusAndExpectedReturnDateBefore(
                        RentalStatus.ACTIVE,
                        LocalDate.now()
                );

        for (Rental rental: rentals){
            if (lateFeeRepository.existsByRental(rental)
            ) {
                continue;
            }

            LateFee lateFee =
                    LateFee.builder()
                            .rental(rental)
                            .dailyAmount(dailyLateFee)
                            .status(LateFeeStatus.ACTIVE)
                            .build();

            lateFeeRepository.save(lateFee);
        }
    }

    public void finalizeLateFee(
            Rental  rental

    ){
        LateFee lateFee =
                lateFeeRepository.findByRental(rental)
                        .orElse(null);

        if (lateFee == null) {
            return;
        }

        if (lateFee.getStatus() != LateFeeStatus.ACTIVE){
            return;
        }

        long daysLate = ChronoUnit.DAYS.between(
                rental.getExpectedReturnDate(),
                rental.getReturnedDate().toLocalDate()
        );

        if (daysLate <= 0){
            return;
        }

        BigDecimal totalAmount =
                dailyLateFee.multiply(
                        BigDecimal.valueOf(daysLate)
                );

        lateFee.setDaysLate(daysLate);
        lateFee.setTotalAmount(totalAmount);
        lateFee.setStatus(LateFeeStatus.PENDING);

        lateFeeRepository.save(lateFee);
    }

    public LateFeeResponse payLateFee(
            Long id,
            PayLateFeeRequest payLateFeeRequest
    ){
        LateFee lateFee =
                lateFeeRepository.findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Multa no encontrada."
                                )
                        );


        if (lateFee.getStatus() != LateFeeStatus.PENDING){
            throw new BadRequestException(
                    "Solo las multas en estado PENDING pueden registrarse como pagadas."
            );
        }

        lateFee.setStatus(LateFeeStatus.PAID);
        lateFee.setPaymentDate(LocalDateTime.now());
        lateFee.setPaymentMethod(payLateFeeRequest.paymentMethod());
        lateFee.setObservations(payLateFeeRequest.observations());

        lateFeeRepository.save(lateFee);

        return mapToResponse(lateFee);
    }
}
