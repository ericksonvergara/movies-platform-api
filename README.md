# Movies Platform

Sistema de alquiler de películas desarrollado con Spring Boot, React y PostgreSQL, implementando arquitectura hexagonal para garantizar escalabilidad, mantenibilidad y separación de responsabilidades.

## 🚀 Tecnologías

### Backend
- Java 17
- Spring Boot
- Spring Security
- Maven
- PostgreSQL
- JWT Authentication
- Arquitectura Hexagonal

### Frontend
- React
- Axios
- React Router

## 🔐 Funcionalidades iniciales
- Registro de usuarios
- Inicio de sesión
- Gestión de roles
- Autenticación con Spring Security
- Encriptación de contraseñas con BCrypt
- Manejo global de excepciones
- API REST

## 🏗 Arquitectura
El proyecto implementa Arquitectura Hexagonal (Ports & Adapters), separando:

- Domain
- Application
- Infrastructure

Con el objetivo de desacoplar la lógica de negocio de frameworks y tecnologías externas.

## 📦 Base de datos
Motor de base de datos utilizado:

- PostgreSQL

## 👥 Roles del sistema
- ADMIN
- EMPLOYEE
- CLIENT

## ⚙️ Ejecución del proyecto

### 1️⃣ Clonar el repositorio

```bash
git clone URL_DEL_REPOSITORIO
```

---

### 2️⃣ Ingresar al directorio del proyecto

Ejemplo en Windows:

```bash
cd C:\Users\TuUsuario\Documents\movies-platform
```

Ejemplo en Linux/Mac:

```bash
cd /home/usuario/projects/movies-platform
```

---

### 3️⃣ Ejecutar el backend

```bash
./mvnw spring-boot:run
```

Si estás en Windows y el comando anterior falla:

```bash
mvnw.cmd spring-boot:run
```

---

### 4️⃣ Acceder al proyecto

Backend disponible en:

```text
http://localhost:8080
```

## 📌 Estado del proyecto

🚧 En desarrollo.
