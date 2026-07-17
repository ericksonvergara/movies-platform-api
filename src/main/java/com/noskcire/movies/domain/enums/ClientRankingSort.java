package com.noskcire.movies.domain.enums;

import lombok.Getter;

@Getter
public enum ClientRankingSort {
    RENTALS("COUNT(DISTINCT r.id)"),
    MOVIES("SUM(rd.quantity)"),
    REVENUE("SUM(rd.quantity * rd.rentalPrice)");

    private final String expression;

    ClientRankingSort(String expression){
        this.expression = expression;
    }


}
