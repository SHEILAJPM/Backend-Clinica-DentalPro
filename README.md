# Backend - Clínica DentalPro

API REST desarrollada con **Spring Boot 3.4.5** para la gestión integral de una clínica dental. Incluye autenticación JWT, control de roles, manejo de citas, atenciones y generación de reportes en PDF.

---
## FRONTEND
https://github.com/JuanVictorFY/Frontend-Clinica-DentalPro.git

## Tecnologías

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 3.4.5 |
| Spring Security | (incluido en Boot) |
| Spring Data JPA / Hibernate | (incluido en Boot) |
| PostgreSQL (Neon) | — |
| JWT (JJWT) | 0.12.6 |
| JavaMail (Gmail SMTP) | (incluido en Boot) |
| Maven | — |

---

## Requisitos previos

- Java 21+
- Maven 3.9+
- Cuenta en [Neon](https://neon.tech) u otra instancia PostgreSQL
- Cuenta Gmail con contraseña de aplicación (para recuperación de contraseña)

---

## Configuración

Edita `src/main/resources/application.properties` con tus propios valores:

```properties
spring.application.name=Backend-Clinica-DentalPro
server.port=8080

# Base de datos (Neon PostgreSQL)
spring.datasource.url=jdbc:postgresql://<host>/<db>?sslmode=require&channel_binding=require
spring.datasource.username=<usuario>
spring.datasource.password=<contraseña>
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA / Hibernate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jackson.serialization.write-dates-as-timestamps=false

# JWT
jwt.secret=<clave-secreta-minimo-32-caracteres>
jwt.expiration=86400000

# CORS (origen del frontend)
app.cors.allowed-origins=http://localhost:4200

# Email (Gmail SMTP)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=<tu-correo>@gmail.com
spring.mail.password=<contraseña-de-aplicacion>
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true

# En true el código de recuperación se devuelve en la respuesta JSON (desarrollo)
app.mail.dev-mode=false
```

---

## Levantar el proyecto

```bash
# Clonar y compilar
git clone https://github.com/SHEILAJPM/Backend-Clinica-DentalPro.git
# Entrar a la carpeta
cd Backend-Clinica-DentalPro
# Ejecutar
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

---

## Usuarios por defecto

Al iniciar la aplicación por primera vez se crean automáticamente los siguientes usuarios:

| Nombre | Email | Contraseña | Rol |
|---|---|---|---|
| Administrador General | admin@dental.com | Admin123! | ADMINISTRADOR |
| Recepcionista | recepcion@dental.com | Admin123! | RECEPCIONISTA |
| Dr. Odontólogo | doctor@dental.com | Admin123! | ODONTOLOGO |

> Cambia las contraseñas tras el primer inicio de sesión.

---

## Autenticación

La API usa **JWT Bearer Token**. El token se obtiene en los endpoints de login y debe enviarse en cada petición protegida:

```
Authorization: Bearer <token>
```

**Duración del token:** 24 horas.

**Claims del token:**
| Claim | Descripción |
|---|---|
| `sub` | Email del usuario |
| `rol` | Rol (ADMINISTRADOR, RECEPCIONISTA, ODONTOLOGO, PACIENTE) |
| `userId` | ID del usuario |
| `iat` | Fecha de emisión |
| `exp` | Fecha de expiración |

---

## Roles y permisos

| Endpoint | ADMINISTRADOR | RECEPCIONISTA | ODONTOLOGO |
|---|:---:|:---:|:---:|
| `/api/usuarios/**` | ✅ | ❌ | ❌ |
| `GET /api/pacientes/**` | ✅ | ✅ | ✅ |
| `POST/PUT/DELETE /api/pacientes/**` | ✅ | ✅ | ❌ |
| `/api/citas/**` | ✅ | ✅ | ✅ |
| `/api/atenciones/**` | ✅ | ❌ | ✅ |
| `/api/reportes/**` | ✅ | ❌ | ✅ |

---

## Endpoints de la API

### Autenticación — `POST /api/auth/**` (públicos)

#### Iniciar sesión (personal)
```
POST /api/auth/login
```
```json
// Request
{ "email": "admin@dental.com", "password": "Admin123!" }

// Response 200
{
  "token": "eyJ...",
  "user": { "id": 1, "nombreCompleto": "...", "email": "...", "rol": "ADMINISTRADOR" }
}
```

#### Iniciar sesión (paciente)
```
POST /api/auth/login-paciente
```
Misma estructura que el login de personal.

#### Registrar paciente
```
POST /api/auth/registro-paciente
```
```json
// Request
{
  "nombreCompleto": "Juan Pérez",
  "dni": "12345678",
  "fechaNacimiento": "1990-05-15",
  "telefono": "987654321",
  "email": "juan@email.com",
  "password": "miPassword123"
}
// Response 201
{ "mensaje": "Paciente registrado exitosamente" }
```

#### Recuperación de contraseña
```
POST /api/auth/forgot-password    { "email": "..." }
POST /api/auth/verify-code        { "email": "...", "code": "123456" }
POST /api/auth/reset-password     { "email": "...", "code": "123456", "newPassword": "..." }
```

---

### Usuarios — `/api/usuarios` (solo ADMINISTRADOR)

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/usuarios` | Listar todos los usuarios |
| `GET` | `/api/usuarios/odontologos` | Listar solo odontólogos |
| `GET` | `/api/usuarios/{id}` | Obtener usuario por ID |
| `POST` | `/api/usuarios` | Crear usuario |
| `PUT` | `/api/usuarios/{id}` | Actualizar usuario |
| `DELETE` | `/api/usuarios/{id}` | Eliminar usuario |

**UsuarioDto:**
```json
{
  "id": 1,
  "nombreCompleto": "Dr. García",
  "email": "garcia@dental.com",
  "password": "Admin123!",
  "rol": "ODONTOLOGO",
  "activo": true
}
```

---

### Pacientes — `/api/pacientes`

| Método | Ruta | Descripción | Acceso |
|---|---|---|---|
| `GET` | `/api/pacientes?page=1&size=10` | Listar paginado | Admin, Recepción, Odontólogo |
| `GET` | `/api/pacientes/buscar?q=texto` | Buscar por nombre o DNI | Admin, Recepción, Odontólogo |
| `GET` | `/api/pacientes/{id}` | Obtener por ID | Admin, Recepción, Odontólogo |
| `POST` | `/api/pacientes` | Crear paciente | Admin, Recepción |
| `PUT` | `/api/pacientes/{id}` | Actualizar paciente | Admin, Recepción |
| `DELETE` | `/api/pacientes/{id}` | Eliminar paciente | Admin, Recepción |

**Respuesta paginada `GET /api/pacientes`:**
```json
{
  "content": [ { "id": 1, "nombreCompleto": "...", "dni": "12345678", ... } ],
  "totalElements": 50,
  "totalPages": 5,
  "currentPage": 1,
  "size": 10
}
```

**PacienteDto:**
```json
{
  "id": 1,
  "nombreCompleto": "María López",
  "dni": "12345678",
  "fechaNacimiento": "1985-03-20",
  "telefono": "987000111",
  "email": "maria@email.com"
}
```

---

### Citas — `/api/citas`

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/citas?fecha=YYYY-MM-DD` | Listar por fecha |
| `GET` | `/api/citas?pacienteId={id}` | Listar por paciente |
| `GET` | `/api/citas?fecha=...&odontologoId={id}` | Listar por odontólogo y fecha |
| `GET` | `/api/citas/disponibilidad?odontologoId=&fecha=&hora=` | Verificar disponibilidad |
| `GET` | `/api/citas/{id}` | Obtener por ID |
| `POST` | `/api/citas` | Crear cita |
| `PUT` | `/api/citas/{id}` | Actualizar cita (→ estado REAGENDADO) |
| `PATCH` | `/api/citas/{id}/cancelar` | Cancelar cita (→ estado CANCELADO) |
| `PATCH` | `/api/citas/{id}/estado` | Cambiar estado manualmente |
| `PUT` | `/api/citas/{id}/finalizar` | Marcar como atendida (→ estado ATENDIDO) |

**Estados válidos:** `PENDIENTE` → `ATENDIDO` / `CANCELADO` / `REAGENDADO`

**CitaDto:**
```json
{
  "id": 1,
  "pacienteId": 5,
  "pacienteNombre": "María López",
  "odontologoId": 2,
  "odontologoNombre": "Dr. García",
  "fecha": "2025-06-01",
  "hora": "10:00",
  "motivo": "Limpieza dental",
  "estado": "PENDIENTE"
}
```

**Verificar disponibilidad:**
```
GET /api/citas/disponibilidad?odontologoId=2&fecha=2025-06-01&hora=10:00
→ true  (disponible)
→ false (ya tiene cita en ese horario)
```

---

### Atenciones — `/api/atenciones` (Admin y Odontólogo)

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/api/atenciones` | Registrar atención |
| `GET` | `/api/atenciones?pacienteId={id}` | Historial de un paciente |
| `GET` | `/api/atenciones/cita/{citaId}` | Atención de una cita específica |

**AtencionDto:**
```json
{
  "id": 1,
  "citaId": 10,
  "pacienteId": 5,
  "pacienteNombre": "María López",
  "odontologoNombre": "Dr. García",
  "diagnostico": "Caries en molar superior",
  "tratamiento": "Empaste de resina",
  "observaciones": "Control en 6 meses",
  "fecha": "2025-06-01"
}
```

---

### Reportes — `/api/reportes` (Admin y Odontólogo)

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/reportes?page=1&size=10` | Listar reportes paginados |
| `GET` | `/api/reportes/{id}` | Obtener reporte por ID |
| `GET` | `/api/reportes/cita/{citaId}` | Reporte de una cita |
| `POST` | `/api/reportes/generar/{citaId}` | Generar reporte desde una cita |
| `GET` | `/api/reportes/{id}/pdf` | Descargar reporte como PDF |

**Descarga de PDF:**
```
GET /api/reportes/{id}/pdf
Content-Type: application/pdf
Content-Disposition: attachment; filename=reporte-{id}.pdf
```

---

## Estructura del proyecto

```
src/main/java/com/dentalpro/
├── auth/               # Login, registro y recuperación de contraseña
├── cita/               # Gestión de citas (entidad, servicio, controlador, DTO)
├── paciente/           # Gestión de pacientes
├── usuario/            # Gestión de usuarios del personal
├── atencion/           # Registros de atención clínica
├── reporte/            # Generación y descarga de reportes PDF
└── config/
    ├── SecurityConfig.java    # Configuración de Spring Security y CORS
    ├── JwtUtil.java           # Generación y validación de tokens JWT
    ├── JwtAuthFilter.java     # Filtro HTTP para autenticación JWT
    ├── DataInitializer.java   # Seeder de usuarios por defecto
    └── EmailService.java      # Servicio de envío de correos
```

---

## Modelo de datos

```
Usuario ──────────────────┐
  id, nombreCompleto,      │ odontologo_id
  email, password,         │
  rol, activo              │
                           ↓
Paciente ──────────── Cita ──────────── Atencion
  id, nombreCompleto,   id, fecha,        id, citaId,
  dni, fechaNacimiento, hora, motivo,     diagnostico,
  telefono, email       estado            tratamiento,
                           │              observaciones,
                           │              fecha
                           └──────────── Reporte
                                          id, citaId,
                                          pacienteNombre,
                                          diagnostico,
                                          tratamiento,
                                          fecha
```

---

## Códigos de respuesta HTTP

| Código | Descripción |
|---|---|
| `200 OK` | Petición exitosa |
| `201 Created` | Recurso creado |
| `204 No Content` | Operación exitosa sin cuerpo de respuesta |
| `400 Bad Request` | Datos de entrada inválidos |
| `401 Unauthorized` | Token ausente, inválido o expirado |
| `403 Forbidden` | Sin permisos para el recurso |
| `404 Not Found` | Recurso no encontrado |
| `409 Conflict` | Conflicto (ej. DNI o email duplicado) |
| `500 Internal Server Error` | Error interno del servidor |


## 🛠️ Autoría y Créditos

Este proyecto fue diseñado, desarrollado e implementado en su totalidad por:

* **Desarrolladora:** Sheila JPM
* **LinkedIn:** [Sheila Jacqueline Principe Merino](https://www.linkedin.com/in/sheila-jacqueline-principe-merino-1579802aa/)
* **GitHub:** [@SHEILAJPM](https://github.com/SHEILAJPM)
* **Contacto:** [principemerinosheila@Gmail.com](mailto:tu-correo@email.com)

---
*Proyecto desarrollado con fines de portafolio y demostración técnica en Ingeniería de Sistemas e Informática.*
