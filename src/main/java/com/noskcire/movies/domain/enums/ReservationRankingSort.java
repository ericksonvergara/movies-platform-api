package com.noskcire.movies.domain.enums;

import lombok.Getter;

@Getter
public enum ReservationRankingSort {
    COUNT("COUNT(r.id)"),
    ACTIVE("""
            SUM(
                CASE
                    WHEN r.status = com.noskcire.movies.domain.enums.ReservationStatus.ACTIVE
                    THEN 1
                    ELSE 0
                END
            )
            """
    ),

    NOTIFIED(
            """
            SUM(
                CASE
                    WHEN r.status = com.noskcire.movies.domain.enums.ReservationStatus.NOTIFIED
                    THEN 1
                    ELSE 0
                END
            )
            """
    ),

    FULFILLED(
            """
            SUM(
                CASE
                    WHEN r.status = com.noskcire.movies.domain.enums.ReservationStatus.FULFILLED
                    THEN 1
                    ELSE 0
                END
            )
            """
    ),

    CANCELLED(
            """
            SUM(
                CASE
                    WHEN r.status = com.noskcire.movies.domain.enums.ReservationStatus.CANCELLED
                    THEN 1
                    ELSE 0
                END
            )
            """
    ),

    EXPIRED(
            """
            SUM(
                CASE
                    WHEN r.status = com.noskcire.movies.domain.enums.ReservationStatus.EXPIRED
                    THEN 1
                    ELSE 0
                END
            )
            """
    );

    private final String expression;

    ReservationRankingSort(String expression){
        this.expression = expression;
    }
}
