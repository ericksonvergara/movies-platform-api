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
  <img src="https://img.shields.io/badge/PostgreSQL-14-blue">
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

---

# 📊 Modelo de negocio actual

```text
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
```

---

# 🗄 Base de datos

Motor utilizado:

* PostgreSQL 14

Entidades principales:

* users
* persons
* roles
* movies
* rentals
* rental_details
* late_fees
* reservations

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

* Reservas de películas
* Dashboard administrativo
* Reportes
* Historial de alquileres
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

🚧 Reportes

🚧 Dashboard
