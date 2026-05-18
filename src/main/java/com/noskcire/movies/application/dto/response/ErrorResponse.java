package com.noskcire.movies.application.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(
        Integer status,
        String message,
        LocalDateTime timestamp
) {
}
