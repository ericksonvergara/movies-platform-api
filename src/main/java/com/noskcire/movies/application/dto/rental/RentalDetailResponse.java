package com.noskcire.movies.application.dto.rental;

import java.math.BigDecimal;

public record RentalDetailResponse(

        Long movieId,
        String movieTitle,
        Integer quantity,
        BigDecimal rentalPrice,
        BigDecimal subtotal
) {
}
