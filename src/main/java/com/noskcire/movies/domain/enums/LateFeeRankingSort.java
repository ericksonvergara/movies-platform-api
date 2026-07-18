package com.noskcire.movies.domain.enums;

import lombok.Getter;

@Getter
public enum LateFeeRankingSort {
    COUNT("COUNT(lf.id)"),
    AMOUNT("SUM(lf.totalAmount)"),
    PENDING("SUM(CASE WHEN lf.status = 'PENDING' THEN 1 ELSE 0 END)");

    private final String expression;

    LateFeeRankingSort(String expression){
        this.expression = expression;
    }
}
