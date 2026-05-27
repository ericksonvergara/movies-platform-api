package com.noskcire.movies.application.dto.response;

import java.time.LocalDateTime;

public record ApiResponse<T>(
        Boolean success,
        String message,
        T data,
        LocalDateTime timestamp
) {
}
