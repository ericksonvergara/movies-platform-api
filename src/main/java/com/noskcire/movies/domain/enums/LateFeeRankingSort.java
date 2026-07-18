package com.noskcire.movies.domain.enums;

import lombok.Getter;

@Getter
public enum LateFeeRankingSort {
    COUNT("COUNT(lf.id)"),
    AMOUNT("COALESCE(SUM(lf.totalAmount), 0)"),
    PENDING("SUM(CASE WHEN lf.status = 'PENDING' THEN 1 ELSE 0 END)"),
    PAID("""
            SUM(
                CASE
                    WHEN lf.status = com.noskcire.movies.domain.enums.LateFeeStatus.PAID
                    THEN 1
                    ELSE 0
                END
            )
            """);

    private final String expression;

    LateFeeRankingSort(String expression) {
        this.expression = expression;
    }
}
