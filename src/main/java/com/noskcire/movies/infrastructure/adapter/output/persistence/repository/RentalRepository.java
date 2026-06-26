package com.noskcire.movies.infrastructure.adapter.output.persistence.repository;

import com.noskcire.movies.domain.enums.RentalStatus;
import com.noskcire.movies.domain.model.Person;
import com.noskcire.movies.domain.model.Rental;
import com.noskcire.movies.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByClient(Person client);
    List<Rental> findByEmployee(User employee);
    List<Rental> findByStatus(RentalStatus status);
    List<Rental> findByStatusAndExpectedReturnDateBefore(
            RentalStatus status,
            LocalDate date
    );
    List<Rental> findByEmployeeId(Long employeeId);

}
