package com.noskcire.movies.application.dto.movie;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovieResponse(
        Long id,
        String title,
        String description,
        String gender,
        Integer releaseYear,
        Integer stock,
        BigDecimal rentalPrice,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}
