package com.noskcire.movies.infrastructure.adapter.output.report;

public final class ReportTitles {

    private ReportTitles() {
        throw new IllegalStateException("Utility class");
    }

    public static final String MOVIE_RANKING =
            "Ranking de Películas";

    public static final String CLIENT_RANKING =
            "Ranking de Clientes";

    public static final String LATE_FEE_RANKING =
            "Ranking de Multas";

    public static final String RESERVATION_RANKING =
            "Ranking de Reservas";

    public static final String INCOME_BY_PERIOD =
            "Ingresos por Período";

    public static final String RENTALS_BY_PERIOD =
            "Alquileres por Período";

    public static final String PROFITABLE_MOVIES =
            "Películas Más Rentables";

    public static final String STATISTICS =
            "Estadísticas Generales";
}
