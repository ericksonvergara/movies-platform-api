package com.noskcire.movies.application.dto.movie;

import java.math.BigDecimal;

public record UpdateMovieRequest(
        String title,
        String description,
        String gender,
        Integer releaseYear,
        BigDecimal rentalPrice
) {
}
