
---

# HU-REP-010 – Exportación de reportes

## Objetivo

Implementar la funcionalidad para exportar los reportes del módulo de reportes en formato Excel (.xlsx), reutilizando la lógica existente del servicio y manteniendo la arquitectura por capas del proyecto.

## Alcance

Implementar la exportación de los siguientes reportes:

* Ranking de películas.
* Ranking de clientes.
* Ranking de multas.
* Ranking de reservas.
* Ingresos por período.
* Alquileres por período.
* Películas más rentables.
* Estadísticas generales.

La exportación deberá realizarse en formato **Excel (.xlsx)**.

No implementar PDF en esta historia de usuario.

---

## Requisitos técnicos

* Mantener la arquitectura actual del proyecto.
* No duplicar la lógica de negocio existente.
* El controlador únicamente deberá invocar al servicio.
* El servicio obtendrá la información utilizando los métodos ya implementados.
* La generación del archivo Excel deberá realizarse en una clase especializada.
* Utilizar Apache POI para generar el archivo.
* No modificar la lógica existente de los reportes.

---

## Arquitectura esperada

```text
ReportController
        │
AnalyticsReportService
        │
AnalyticsReportRepository
        │
ExcelReportExporter
```

Crear una clase dedicada:

```
ExcelReportExporter
```

Responsable únicamente de construir el archivo Excel.

---

## Endpoints

Implementar un endpoint por tipo de reporte.

Ejemplos:

```
GET /api/reports/export/movies
GET /api/reports/export/customers
GET /api/reports/export/late-fees
GET /api/reports/export/reservations
GET /api/reports/export/income
GET /api/reports/export/rentals
GET /api/reports/export/profitable-movies
GET /api/reports/export/statistics
```

Cada endpoint deberá devolver un archivo descargable.

---

## Respuesta HTTP

Retornar:

* HTTP 200
* Content-Type correspondiente a Excel

```
application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
```

Agregar encabezados para descarga:

```
Content-Disposition:
attachment;
filename=<nombre_reporte>.xlsx
```

---

## Restricciones

* No escribir consultas SQL nuevas para la exportación.
* Reutilizar los métodos existentes del servicio.
* No duplicar DTO.
* No crear repositorios adicionales.
* No modificar la estructura de los reportes existentes.

---

## Dependencia

Agregar Apache POI si aún no existe.


---

## Criterios de aceptación

* Exportar correctamente todos los reportes existentes.
* El archivo Excel debe contener encabezados.
* El archivo debe contener todos los registros del reporte.
* El nombre del archivo debe identificar el reporte exportado.
* No duplicar lógica existente.
* Mantener separación de responsabilidades.
* Seguir la arquitectura utilizada en el proyecto.
* Código limpio y reutilizable.
