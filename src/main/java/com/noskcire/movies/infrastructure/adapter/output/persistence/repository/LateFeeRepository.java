package com.noskcire.movies.infrastructure.adapter.output.persistence.repository;

import com.noskcire.movies.domain.enums.LateFeeStatus;
import com.noskcire.movies.domain.model.LateFee;
import com.noskcire.movies.domain.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LateFeeRepository extends JpaRepository<LateFee, Long> {

    Optional<LateFee> findByRental(Rental rental);
    List<LateFee> findByStatus(LateFeeStatus status);
}