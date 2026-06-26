package com.noskcire.movies.application.service;

import com.noskcire.movies.application.dto.rental.CreateRentalDetailRequest;
import com.noskcire.movies.application.dto.rental.CreateRentalRequest;
import com.noskcire.movies.application.dto.rental.RentalDetailResponse;
import com.noskcire.movies.application.dto.rental.RentalResponse;
import com.noskcire.movies.domain.enums.PersonType;
import com.noskcire.movies.domain.enums.RentalStatus;
import com.noskcire.movies.domain.exception.BadRequestException;
import com.noskcire.movies.domain.exception.ResourceNotFoundException;
import com.noskcire.movies.domain.model.*;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RentalService {

    private final RentalRepository rentalRepository;
    private final RentalDetailRepository rentalDetailRepository;
    private final PersonRepository personRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    private User getAuthenticatedEmployee(){
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String username = authentication.getName();

        return userRepository
                .findByUsername(username)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Empleado no encontrado."
                        )
                );
    }

    public RentalResponse createRental(
            CreateRentalRequest createRentalRequest
    ){

        Person client = personRepository
                .findById(createRentalRequest.clientId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Cliente no encontrado."
                        )
                );

        if (client.getType() != PersonType.CLIENT){
            throw new BadRequestException(
                    "La persona seleccionada no es un cliente."
            );
        }

        User employee = getAuthenticatedEmployee();
        if (!employee.isEnabled()){
            throw new BadRequestException(
                    "El empleado se encuentra deshabilitado."
            );
        }

        Rental rental = Rental.builder()
                .client(client)
                .employee(employee)
                .rentalDate(LocalDate.now())
                .expectedReturnDate(createRentalRequest.expectedReturnDate())
                .status(RentalStatus.ACTIVE)
                .total(BigDecimal.ZERO)
                .build();

        rentalRepository.save(rental);

        BigDecimal total = BigDecimal.ZERO;
        List<RentalDetail> details = new ArrayList<>();

        for (
                CreateRentalDetailRequest detailRequest
                : createRentalRequest.movies()
        ){
            Movie movie = movieRepository
                    .findById(detailRequest.movieId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException(
                                    "Pelicula no encontrada."
                            )
                    );

            if (!movie.isEnabled()) {
                throw new BadRequestException(
                        "La película se encuentra deshabilitada."
                );
            }



            if (
                    movie.getStock()
                    < detailRequest.quantity()
            ){
                throw new BadRequestException(
                        "No hay stock suficiente para la pelicula: "
                                + movie.getTitle()
                );
            }

            BigDecimal subtotal =
                    movie.getRentalPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            detailRequest.quantity()
                                    )
                            );

            total = total.add(subtotal);

            movie.setStock(
                    movie.getStock()
                    - detailRequest.quantity()
            );
            movieRepository.save(movie);

            RentalDetail detail =
                    RentalDetail.builder()
                            .rental(rental)
                            .movie(movie)
                            .quantity(detailRequest.quantity())
                            .rentalPrice(
                                    movie.getRentalPrice()
                            )
                            .build();

            details.add(detail);
        }

        rentalDetailRepository.saveAll(details);

        rental.setTotal(total);

        rentalRepository.save(rental);

        return mapToResponse(rental);

    }

    private RentalResponse mapToResponse(
            Rental rental
    ) {
        List<RentalDetail> details =
                rentalDetailRepository
                        .findByRental(rental);

        List<RentalDetailResponse> detailResponses =
                details.stream()
                        .map(detail -> {

                            BigDecimal subtotal =
                                    detail.getRentalPrice()
                                            .multiply(
                                                    BigDecimal.valueOf(
                                                            detail.getQuantity()
                                                    )
                                            );

                            return new RentalDetailResponse(
                                    detail.getMovie().getId(),
                                    detail.getMovie().getTitle(),
                                    detail.getQuantity(),
                                    detail.getRentalPrice(),
                                    subtotal
                            );

                        })
                        .toList();

        return new RentalResponse(
                rental.getId(),
                rental.getClient().getId(),
                rental.getClient().getNames()
                        + " "
                        + rental.getClient().getLastNames(),
                rental.getEmployee().getUsername(),
                rental.getRentalDate(),
                rental.getExpectedReturnDate(),
                rental.getReturnedDate(),
                rental.getStatus(),
                rental.getTotal(),
                detailResponses
        );
    }

    public RentalResponse getRentalById(
            Long id
    ){
        Rental rental =
                rentalRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Renta no encontrada."
                                )
                        );
        return mapToResponse(rental);
    }

    public List<RentalResponse> getAllRentals(){
        List<Rental> rentals = rentalRepository.findAll();
        return rentals.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<RentalResponse> getRentalActive(){
        List<Rental> rentals =
                rentalRepository
                        .findByStatus(
                                RentalStatus.ACTIVE
                        );
        return rentals.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<RentalResponse> getRentalReturned(){
        List<Rental> rentals =
                rentalRepository
                        .findByStatus(
                                RentalStatus.RETURNED
                        );
        return rentals.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<RentalResponse> getRentalsByClientId(
            Long clientId
    ){
        Person client = personRepository
                .findById(clientId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Cliente no encontrado."
                        )
                );
        List<Rental> rentals = rentalRepository.findByClient(client);
        return rentals.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public RentalResponse returnRental(
            Long rentalId
    ){
        Rental rental = rentalRepository
                .findById(rentalId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Renta no encontrada."
                        )
                );

        if (rental.getStatus() != RentalStatus.ACTIVE){
            throw new BadRequestException(
                    "La renta ya ha sido devuelta o no se encuentra activa."
            );
        }

        List<RentalDetail> details =
                rentalDetailRepository.findByRental(rental);

        for (RentalDetail rentalDetail : details){
            Movie movie = rentalDetail.getMovie();

            movie.setStock(
                    movie.getStock() + rentalDetail.getQuantity()
            );

            movieRepository.save(movie);
        }

        rental.setReturnedDate(LocalDateTime.now());
        rental.setStatus(RentalStatus.RETURNED);
        rentalRepository.save(rental);
        return mapToResponse(rental);
    }

    public List<RentalResponse> getOverdueRentals(){
        List<Rental> rentals =
                rentalRepository
                        .findByStatusAndExpectedReturnDateBefore(
                                RentalStatus.ACTIVE,
                                LocalDate.now()
                        );
        return rentals.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<RentalResponse> getRentalsByEmployeeId(
            Long employeeId
    ){
        User employee = userRepository
                .findById(employeeId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Empleado no encontrado."
                        )
                );
        if (!employee.getRole().getName().equals("EMPLOYEE")) {
            throw new BadRequestException(
                    "El usuario no corresponde a un empleado."
            );
        }
        List<Rental> rentals = rentalRepository.findByEmployee(employee);
        return rentals.stream()
                .map(this::mapToResponse)
                .toList();
    }
}
