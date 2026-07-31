<p align="center">
  <img src="https://spring.io/img/projects/spring-boot.svg" width="120" alt="Spring Boot Logo">
</p>

<h1 align="center">
  Movies Platform
</h1>

<p align="center">
  Sistema de gestión y alquiler de películas desarrollado con Spring Boot, PostgreSQL y React.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange">
  <img src="https://img.shields.io/badge/SpringBoot-4.x-green">
  <img src="https://img.shields.io/badge/PostgreSQL-16-blue">
  <img src="https://img.shields.io/badge/JWT-Security-red">
  <img src="https://img.shields.io/badge/Architecture-Hexagonal-purple">
</p>

---

# 📖 Descripción

Movies Platform es una aplicación para la administración de un videoclub, permitiendo gestionar usuarios, clientes, empleados, películas y alquileres mediante una API REST segura basada en JWT.

El proyecto implementa Arquitectura Hexagonal (Ports & Adapters) para garantizar desacoplamiento, mantenibilidad y escalabilidad.

---

# 🚀 Tecnologías

## Backend

* Java 17
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate
* JWT Authentication
* Maven
* PostgreSQL
* Lombok
* Springdoc OpenAPI (Swagger UI)
* Apache POI (Exportación Excel)
* Docker / Docker Compose

## Frontend

* React
* Axios
* React Router

---

# 🏗 Arquitectura

El proyecto sigue una Arquitectura Hexagonal organizada en:

```text
src/main/java
│
├── application
│   ├── dto
│   ├── service
│
├── domain
│   ├── model
│   ├── enums
│   ├── exception
│
├── infrastructure
│   ├── adapter
│   │   ├── input
│   │   └── output
│
└── config
```

Principios aplicados:

* Separación de responsabilidades
* Inversión de dependencias
* Bajo acoplamiento
* Alta cohesión

---

# 🔐 Seguridad

La aplicación implementa:

* JWT Access Token
* JWT Refresh Token
* Spring Security
* BCrypt Password Encoder
* Control de roles
* Usuarios habilitados/deshabilitados
* Protección de endpoints mediante autorización

---

# 👥 Roles del sistema

## ADMIN

Puede:

* Gestionar usuarios
* Gestionar clientes
* Gestionar empleados
* Gestionar películas
* Gestionar alquileres

## EMPLOYEE

Puede:

* Registrar clientes
* Gestionar alquileres
* Registrar devoluciones
* Consultar películas

## CLIENT

Puede:

* Registrarse en la plataforma
* Iniciar sesión
* Realizar reservas (próximamente)

---

# 📦 Módulos implementados

## Autenticación

* Registro de usuarios
* Inicio de sesión
* Refresh Token
* Gestión de roles
* Activación y desactivación de usuarios

## Personas

* Crear personas
* Actualizar personas
* Consultar personas
* Clientes independientes de usuarios

## Usuarios

* Crear usuarios
* Actualizar roles
* Activar usuarios
* Desactivar usuarios

## Películas

* Crear películas
* Actualizar películas
* Consultar películas
* Control de stock
* Habilitar y deshabilitar películas

## Alquileres

* Registrar alquileres
* Validar disponibilidad de stock
* Calcular totales
* Registrar devoluciones
* Restituir inventario
* Control de estado de alquiler

## Gestión de Multas

* Generación automática de multas por retraso.
* Cálculo dinámico del valor diario.
* Congelación automática al devolver la renta.
* Registro del pago de multas.
* Consulta por estado (ACTIVE, PENDING, PAID).
* Auditoría completa de creación y actualización.

## Módulo de Reservas

* Creación de reservas de películas sin disponibilidad.
* Cancelación de reservas activas.
* Gestión de la cola de reservas mediante el algoritmo FIFO (First In, First Out).
* Activación automática de la siguiente reserva cuando una película es devuelta.
* Expiración automática de reservas notificadas al vencer el tiempo límite para reclamar la película.
* Activación automática del siguiente cliente en la cola cuando una reserva expira.
* Finalización automática de la reserva cuando el cliente realiza el alquiler de la película.
* Control de acceso basado en roles (Administrador, Empleado y Cliente).

## Reportes

* Dashboard general con estadísticas de películas, clientes, alquileres, multas, reservas e ingresos.
* Rankings de películas, clientes, multas y reservas con ordenamiento configurable.
* Películas más rentables.
* Ingresos y alquileres por período.
* Tendencia de alquileres.
* Estadísticas generales del sistema.
* Exportación de reportes en Excel (.xlsx) mediante Apache POI.

## Dashboard

* Métricas agregadas de películas, personas, alquileres, multas, reservas e ingresos.
* Tendencia de alquileres por período.
* Indicadores de rendimiento del videoclub.

---

# 📊 Modelo de negocio actual

```text
Role
│
└── Roles del sistema (ADMIN, EMPLOYEE, CLIENT)

Person
│
├── CLIENT
├── EMPLOYEE
└── ADMIN

User
│
└── Acceso al sistema

Movie
│
└── Inventario

Rental
│
└── Alquiler

RentalDetail
│
└── Películas alquiladas

LateFee
│
└── Multas por retraso

Reservation
│
└── Reservas de películas

Token
│
└── Tokens de sesión (access / refresh)
```

## Entidades y relaciones

| Entidad | Atributos principales | Relaciones |
|---------|----------------------|------------|
| **Person** | id, names, lastNames, document, phone, email, address, dateBirth, type, deleted | 1:1 con User |
| **User** | id, username, password, enabled | 1:1 con Person · N:1 con Role · 1:N con Token |
| **Role** | id, name | 1:N con User |
| **Movie** | id, title, description, gender, releaseYear, stock, rentalPrice, enabled, deleted | 1:N con RentalDetail · 1:N con Reservation |
| **Rental** | id, rentalDate, expectedReturnDate, returnedDate, status, total, deleted | N:1 con User (employee) · N:1 con Person (client) · 1:N con RentalDetail · 1:1 con LateFee |
| **RentalDetail** | id, quantity, rentalPrice | N:1 con Rental · N:1 con Movie |
| **LateFee** | id, daysLate, dailyAmount, totalAmount, status, paymentDate, paymentMethod, observations | 1:1 con Rental |
| **Reservation** | id, reservationDate, expirationDate, notificationSent, fulfilledAt, status | N:1 con Movie · N:1 con Person (client) |
| **Token** | id, token, type, revoked, expiresAt | N:1 con User |

## Estados de las entidades

| Entidad | Estados |
|---------|---------|
| Person | `CLIENT` · `EMPLOYEE` · `ADMIN` (tipo de persona) |
| Rental | `ACTIVE` · `RETURNED` · `OVERDUE` · `CANCELLED` · `RESERVED` |
| LateFee | `ACTIVE` · `PENDING` · `PAID` |
| Reservation | `ACTIVE` · `NOTIFIED` · `FULFILLED` · `CANCELLED` · `EXPIRED` |
| PaymentMethod | `CASH` · `CARD` · `TRANSFER` |
| Token | `ACCESS` · `REFRESH` (tipo de token) |

## Reportes y Dashboard

Los reportes no son entidades persistentes: se calculan en tiempo real sobre las entidades anteriores mediante consultas agregadas (proyecciones JPA nativas).

| Reporte | Descripción |
|---------|-------------|
| **Dashboard** | Conteos globales: películas (total, disponibles, rentadas), personas (clientes, empleados), alquileres (activos, devueltos), reservas (activas, notificadas, expiradas), multas (activas, pendientes, pagadas) y resumen de ingresos (alquileres + multas) |
| **Ranking de películas** | Películas más alquiladas, ordenable por cantidad de alquileres o ingresos |
| **Ranking de clientes** | Clientes con más alquileres, ordenable por cantidad o total gastado |
| **Ranking de multas** | Clientes con más multas, ordenable por cantidad o monto total |
| **Ranking de reservas** | Películas con más reservas, ordenable por cantidad |
| **Ingresos por período** | Total de ingresos por alquileres y multas en un rango de fechas |
| **Alquileres por período** | Cantidad de alquileres en un rango de fechas |
| **Tendencia de alquileres** | Evolución de alquileres en un rango de fechas |
| **Películas más rentables** | Películas ordenadas por ingresos generados |
| **Estadísticas generales** | Métricas globales del sistema |

Todos los reportes son exportables a **Excel (.xlsx)** mediante Apache POI.

---

# 🗄 Base de datos

Motor utilizado:

* PostgreSQL 16

Entidades principales:

* users
* persons
* roles
* movies
* rentals
* rental_details
* late_fees
* reservations
* tokens

---

# ⚙️ Ejecución del proyecto

## Clonar repositorio

```bash
git clone URL_DEL_REPOSITORIO
```

## Ingresar al directorio

```bash
cd movies-platform
```

## Configurar base de datos

Actualizar el archivo:

```properties
application.yml
```

con las credenciales de PostgreSQL.

## Ejecutar aplicación

Linux / Mac:

```bash
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

## Ejecutar con Docker

Levantar la aplicación y la base de datos PostgreSQL con Docker Compose:

```bash
docker compose up --build
```

La aplicación quedará disponible en `http://localhost:8088` y PostgreSQL en el puerto `5432` (usuario `movies_user`, contraseña `movies_pass`, base de datos `movies`).

---

# 📚 Documentación API

Swagger UI:

```text
http://localhost:8088/swagger-ui/index.html
```

OpenAPI:

```text
http://localhost:8088/v3/api-docs
```

---

# 🚧 Próximas funcionalidades

* Catálogo público
* Frontend completo en React

---

# 📌 Estado del proyecto

🟢 Backend funcional en desarrollo activo.

Módulos completados:

✅ Seguridad y autenticación

✅ Gestión de personas

✅ Gestión de usuarios

✅ Gestión de películas

✅ Gestión de alquileres

✅ Multas por retraso

✅ Reservas

✅ Reportes

✅ Dashboard

```
movies
├─ .gitattributes
├─ .gitignore
├─ .mvn
│  └─ wrapper
│     └─ maven-wrapper.properties
├─ docker-compose.yml
├─ Dockerfile
├─ HELP.md
├─ mvnw
├─ mvnw.cmd
├─ pom.xml
├─ README.md
└─ src
   ├─ main
   │  ├─ java
   │  │  └─ com
   │  │     └─ noskcire
   │  │        └─ movies
   │  │           ├─ application
   │  │           │  ├─ dto
   │  │           │  │  ├─ auth
   │  │           │  │  │  ├─ AuthResponse.java
   │  │           │  │  │  ├─ LoginRequest.java
   │  │           │  │  │  ├─ LogoutRequest.java
   │  │           │  │  │  ├─ RefreshTokenRequest.java
   │  │           │  │  │  └─ RegisterRequest.java
   │  │           │  │  ├─ lateFee
   │  │           │  │  │  ├─ LateFeeResponse.java
   │  │           │  │  │  └─ PayLateFeeRequest.java
   │  │           │  │  ├─ movie
   │  │           │  │  │  ├─ CreateMovieRequest.java
   │  │           │  │  │  ├─ MovieResponse.java
   │  │           │  │  │  └─ UpdateMovieRequest.java
   │  │           │  │  ├─ person
   │  │           │  │  │  ├─ CreatePersonRequest.java
   │  │           │  │  │  ├─ PersonResponse.java
   │  │           │  │  │  └─ UpdatePersonRequest.java
   │  │           │  │  ├─ rental
   │  │           │  │  │  ├─ CreateRentalDetailRequest.java
   │  │           │  │  │  ├─ CreateRentalRequest.java
   │  │           │  │  │  ├─ OverdueRentalResponse.java
   │  │           │  │  │  ├─ RentalDetailResponse.java
   │  │           │  │  │  └─ RentalResponse.java
   │  │           │  │  ├─ report
   │  │           │  │  │  ├─ ClientRankingResponse.java
   │  │           │  │  │  ├─ ClientRankingResult.java
   │  │           │  │  │  ├─ DashboardResponse.java
   │  │           │  │  │  ├─ IncomeByPeriodResponse.java
   │  │           │  │  │  ├─ LateFeeDashboardResponse.java
   │  │           │  │  │  ├─ LateFeeRankingResponse.java
   │  │           │  │  │  ├─ LateFeeRankingResult.java
   │  │           │  │  │  ├─ MovieDashboardResponse.java
   │  │           │  │  │  ├─ MovieRankingResponse.java
   │  │           │  │  │  ├─ MovieRankingResult.java
   │  │           │  │  │  ├─ PersonDashboardResponse.java
   │  │           │  │  │  ├─ ProfitableMovieResponse.java
   │  │           │  │  │  ├─ ProfitableMovieResult.java
   │  │           │  │  │  ├─ RentalDashboardResponse.java
   │  │           │  │  │  ├─ RentalsByPeriodResponse.java
   │  │           │  │  │  ├─ RentalsByPeriodResult.java
   │  │           │  │  │  ├─ RentalTrendResponse.java
   │  │           │  │  │  ├─ RentalTrendResult.java
   │  │           │  │  │  ├─ ReservationDashboardResponse.java
   │  │           │  │  │  ├─ ReservationRankingResponse.java
   │  │           │  │  │  ├─ ReservationRankingResult.java
   │  │           │  │  │  ├─ RevenueDashboardResponse.java
   │  │           │  │  │  └─ StatisticsResponse.java
   │  │           │  │  ├─ reservation
   │  │           │  │  │  ├─ CreateReservationRequest.java
   │  │           │  │  │  └─ ReservationResponse.java
   │  │           │  │  ├─ response
   │  │           │  │  │  ├─ ApiResponse.java
   │  │           │  │  │  └─ ErrorResponse.java
   │  │           │  │  └─ user
   │  │           │  │     ├─ CreateUserRequest.java
   │  │           │  │     ├─ UpdateUserRequest.java
   │  │           │  │     └─ UserResponse.java
   │  │           │  ├─ service
   │  │           │  │  ├─ AnalyticsReportService.java
   │  │           │  │  ├─ AuthService.java
   │  │           │  │  ├─ LateFeeService.java
   │  │           │  │  ├─ MovieService.java
   │  │           │  │  ├─ PersonService.java
   │  │           │  │  ├─ RankingReportService.java
   │  │           │  │  ├─ RentalService.java
   │  │           │  │  ├─ ReportExportService.java
   │  │           │  │  ├─ ReportService.java
   │  │           │  │  ├─ ReservationService.java
   │  │           │  │  └─ UserService.java
   │  │           │  └─ validation
   │  │           │     ├─ ValidLimit.java
   │  │           │     └─ ValidLimitValidator.java
   │  │           ├─ domain
   │  │           │  ├─ audit
   │  │           │  │  └─ BaseAuditEntity.java
   │  │           │  ├─ enums
   │  │           │  │  ├─ ClientRankingSort.java
   │  │           │  │  ├─ LateFeeRankingSort.java
   │  │           │  │  ├─ LateFeeStatus.java
   │  │           │  │  ├─ MovieProfitabilitySort.java
   │  │           │  │  ├─ MovieRankingSort.java
   │  │           │  │  ├─ PaymentMethod.java
   │  │           │  │  ├─ PersonType.java
   │  │           │  │  ├─ RentalStatus.java
   │  │           │  │  ├─ ReservationRankingSort.java
   │  │           │  │  ├─ ReservationStatus.java
   │  │           │  │  └─ TokenType.java
   │  │           │  ├─ exception
   │  │           │  │  ├─ BadRequestException.java
   │  │           │  │  ├─ ReportExportException.java
   │  │           │  │  └─ ResourceNotFoundException.java
   │  │           │  └─ model
   │  │           │     ├─ LateFee.java
   │  │           │     ├─ Movie.java
   │  │           │     ├─ Person.java
   │  │           │     ├─ Rental.java
   │  │           │     ├─ RentalDetail.java
   │  │           │     ├─ Reservation.java
   │  │           │     ├─ Role.java
   │  │           │     ├─ Token.java
   │  │           │     └─ User.java
   │  │           ├─ infrastructure
   │  │           │  ├─ adapter
   │  │           │  │  ├─ input
   │  │           │  │  │  └─ rest
   │  │           │  │  │     ├─ AuthController.java
   │  │           │  │  │     ├─ LateFeeController.java
   │  │           │  │  │     ├─ MovieController.java
   │  │           │  │  │     ├─ PersonController.java
   │  │           │  │  │     ├─ ProfileController.java
   │  │           │  │  │     ├─ RentalController.java
   │  │           │  │  │     ├─ ReportController.java
   │  │           │  │  │     ├─ ReservationController.java
   │  │           │  │  │     └─ UserController.java
   │  │           │  │  └─ output
   │  │           │  │     ├─ persistence
   │  │           │  │     │  └─ repository
   │  │           │  │     │     ├─ LateFeeRepository.java
   │  │           │  │     │     ├─ MovieRepository.java
   │  │           │  │     │     ├─ PersonRepository.java
   │  │           │  │     │     ├─ RentalDetailRepository.java
   │  │           │  │     │     ├─ RentalRepository.java
   │  │           │  │     │     ├─ report
   │  │           │  │     │     │  ├─ AnalyticsReportRepository.java
   │  │           │  │     │     │  ├─ AnalyticsReportRepositoryImpl.java
   │  │           │  │     │     │  ├─ DashboardReportRepository.java
   │  │           │  │     │     │  ├─ DashboardReportRepositoryImpl.java
   │  │           │  │     │     │  ├─ projection
   │  │           │  │     │     │  │  ├─ ClientRankingProjection.java
   │  │           │  │     │     │  │  ├─ IncomeByPeriodProjection.java
   │  │           │  │     │     │  │  ├─ LateFeeRankingProjection.java
   │  │           │  │     │     │  │  ├─ MovieRankingProjection.java
   │  │           │  │     │     │  │  ├─ ProfitableMovieProjection.java
   │  │           │  │     │     │  │  ├─ RentalsByPeriodProjection.java
   │  │           │  │     │     │  │  ├─ RentalTrendProjection.java
   │  │           │  │     │     │  │  ├─ ReservationRankingProjection.java
   │  │           │  │     │     │  │  ├─ RevenueSummary.java
   │  │           │  │     │     │  │  └─ StatisticsProjection.java
   │  │           │  │     │     │  ├─ RankingReportRepository.java
   │  │           │  │     │     │  ├─ RankingReportRepositoryImpl.java
   │  │           │  │     │     │  ├─ RentalReportRepository.java
   │  │           │  │     │     │  ├─ RentalReportRepositoryImpl.java
   │  │           │  │     │     │  ├─ ReservationReportRepository.java
   │  │           │  │     │     │  ├─ ReservationReportRepositoryImpl.java
   │  │           │  │     │     │  ├─ RevenueReportRepository.java
   │  │           │  │     │     │  └─ RevenueReportRepositoryImpl.java
   │  │           │  │     │     ├─ ReservationRepository.java
   │  │           │  │     │     ├─ RoleRepository.java
   │  │           │  │     │     ├─ TokenRepository.java
   │  │           │  │     │     └─ UserRepository.java
   │  │           │  │     └─ report
   │  │           │  │        ├─ ExcelReportExporter.java
   │  │           │  │        ├─ PdfReportExporter.java
   │  │           │  │        ├─ ReportHeaders.java
   │  │           │  │        └─ ReportTitles.java
   │  │           │  ├─ AuditorAwareImpl.java
   │  │           │  ├─ config
   │  │           │  │  ├─ DataInitializer.java
   │  │           │  │  ├─ JpaAuditConfig.java
   │  │           │  │  └─ OpenApiConfig.java
   │  │           │  ├─ exception
   │  │           │  │  └─ GlobalExceptionHandler.java
   │  │           │  ├─ security
   │  │           │  │  ├─ CustomAccessDeniedHandler.java
   │  │           │  │  ├─ CustomAuthenticationEntryPoint.java
   │  │           │  │  ├─ CustomUserDetailsService.java
   │  │           │  │  ├─ jwt
   │  │           │  │  │  ├─ JwtAuthenticationFilter.java
   │  │           │  │  │  └─ JwtService.java
   │  │           │  │  └─ SecurityConfig.java
   │  │           │  └─ specification
   │  │           │     ├─ GenericSpecification.java
   │  │           │     └─ MovieSpecification.java
   │  │           └─ MoviesApplication.java
   │  └─ resources
   │     ├─ application.yml
   │     ├─ static
   │     └─ templates
   └─ test
      └─ java
         └─ com
            └─ noskcire
               └─ movies
                  ├─ application
                  │  └─ service
                  │     ├─ AnalyticsReportServiceTest.java
                  │     └─ AuthServiceTest.java
                  └─ MoviesApplicationTests.java

```