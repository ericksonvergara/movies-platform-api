package com.noskcire.movies.application.dto.movie;


import java.math.BigDecimal;

public record CreateMovieRequest(
        String title,
        String description,
        String gender,
        Integer releaseYear,
        Integer stock,
        BigDecimal rentalPrice
) {
}
