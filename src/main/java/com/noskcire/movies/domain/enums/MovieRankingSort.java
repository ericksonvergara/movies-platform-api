package com.noskcire.movies.domain.enums;

import lombok.Getter;

@Getter
public enum MovieRankingSort {
    RENTALS("COUNT(DISTINCT r.id)"),
    UNITS("SUM(rd.quantity)"),
    REVENUE("SUM(rd.quantity * rd.rentalPrice)");

    private final String expression;

    MovieRankingSort(String expression) {
        this.expression = expression;
    }

}
