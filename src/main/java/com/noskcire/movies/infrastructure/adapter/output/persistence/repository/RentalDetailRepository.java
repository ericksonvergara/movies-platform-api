package com.noskcire.movies.infrastructure.adapter.output.persistence.repository;

import com.noskcire.movies.domain.model.Rental;
import com.noskcire.movies.domain.model.RentalDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalDetailRepository extends JpaRepository<RentalDetail, Long> {
    List<RentalDetail> findByRental(Rental rental);
}
