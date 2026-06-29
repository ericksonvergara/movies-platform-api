package com.noskcire.movies.application.dto.lateFee;

import com.noskcire.movies.domain.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PayLateFeeRequest(
        @NotNull
        PaymentMethod paymentMethod,

        @Size(max = 500)
        String observations
) {
}
