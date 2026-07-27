package com.noskcire.movies.infrastructure.adapter.output.report;

public final class ReportHeaders {

    private ReportHeaders(){
        throw new IllegalStateException("Utility class");
    }

    public static final String[] MOVIE_RANKING = {
            "ID",
            "Título",
            "Veces Alquilada",
            "Unidades Alquiladas",
            "Promedio Unidades",
            "Ingresos"

    };

    public static final String[] CLIENT_RANKING = {
            "ID Cliente",
            "Nombre",
            "Correo Electrónico",
            "Total Alquileres",
            "Total Gastado"
    };

    public static final String[] LATE_FEE_RANKING = {
            "ID Cliente",
            "Cliente",
            "Total Multas",
            "Multas Activas",
            "Multas Pendientes",
            "Multas Pagadas",
            "Monto Total"
    };

    public static final String[] RESERVATION_RANKING = {
            "ID Cliente",
            "Cliente",
            "Total Reservas",
            "Reservas Activas",
            "Reservas Notificadas",
            "Reservas Cumplidas",
            "Reservas Canceladas",
            "Reservas Expiradas"
    };

    public static final String[] INCOME_BY_PERIOD = {
            "Fecha Inicio",
            "Fecha Fin",
            "Total Alquileres",
            "Ingreso Total",
            "Promedio por Alquiler"
    };

    public static final String[] RENTALS_BY_PERIOD = {
            "Fecha",
            "Total Alquileres"
    };

    public static final String[] PROFITABLE_MOVIES = {
            "ID",
            "Título",
            "Unidades Alquiladas",
            "Ingresos Totales"
    };

    public static final String[] STATISTICS = {
            "Ingreso Total",
            "Total Alquileres",
            "Total Reservas",
            "Total Multas",
            "Total Personas",
            "Película Más Alquilada",
            "Cliente con Más Alquileres"
    };
}
