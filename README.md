# Backend — Clínica DentalPro

API REST desarrollada con **Spring Boot 3.4.5** para la gestión integral de una clínica dental. Incluye autenticación JWT, control de roles, manejo de citas, atenciones, historial clínico, catálogo de tratamientos, pagos y generación de reportes en PDF.

---

## Frontend

https://github.com/JuanVictorFY/Frontend-Clinica-DentalPro.git

---

## Tecnologías

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 3.4.5 |
| Spring Security | incluido en Boot |
| Spring Data JPA / Hibernate | incluido en Boot |
| PostgreSQL (Neon) | — |
| JWT (JJWT) | 0.12.6 |
| JavaMail (Gmail SMTP) | incluido en Boot |
| Maven | 3.9+ |

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
git clone https://github.com/SHEILAJPM/Backend-Clinica-DentalPro.git
cd Backend-Clinica-DentalPro
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

---

## Datos por defecto

Al iniciar la aplicación por primera vez se crean automáticamente:

### Usuarios

| Nombre | Email | Contraseña | Rol |
|---|---|---|---|
| Administrador General | admin@dental.com | 123456 | ADMINISTRADOR |
| Recepcionista | recepcion@dental.com | 123456 | RECEPCIONISTA |
| Dr. Carlos Mendoza | doctor@dental.com | 123456 | ODONTOLOGO |
| Dra. Laura Quispe | doctora@dental.com | 123456 | ODONTOLOGO |

### Pacientes (8 registros)
Ana Torres, Luis García, María Flores, Jorge Castro, Rosa Mamani, Carlos Ríos, Sofía Paredes, Pedro Suárez.

### Otras tablas con datos de ejemplo
- **historial_clinico** — 8 fichas médicas con alergias, condiciones y grupo sanguíneo
- **tratamiento_catalogo** — 8 tratamientos (limpieza, extracción, blanqueamiento, ortodoncia, endodoncia, obturación, implante, profilaxis)
- **citas** — 8 citas en estado ATENDIDO
- **atenciones** — 8 registros clínicos vinculados a las citas
- **reportes** — 8 reportes generados automáticamente
- **pagos** — 8 pagos (5 PAGADO, 3 PENDIENTE)

---

## Autenticación

La API usa **JWT Bearer Token**. El token se obtiene en los endpoints de login y debe enviarse en cada petición protegida:

```
Authorization: Bearer <token>
```

**Duración del token:** 24 horas.

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
| `/api/historial/**` | ✅ | ✅ | ✅ |
| `/api/tratamientos/**` | ✅ | ❌ | ❌ |
| `/api/pagos/**` | ✅ | ✅ | ❌ |

---

## Endpoints de la API

### Autenticación — `POST /api/auth/**` (públicos)

```
POST /api/auth/login               { "email": "...", "password": "..." }
POST /api/auth/login-paciente      { "email": "...", "password": "..." }
POST /api/auth/registro-paciente   { nombreCompleto, dni, fechaNacimiento, telefono, email, password }
POST /api/auth/forgot-password     { "email": "..." }
POST /api/auth/verify-code         { "email": "...", "code": "123456" }
POST /api/auth/reset-password      { "email": "...", "code": "123456", "newPassword": "..." }
```

**Respuesta login:**
```json
{
  "token": "eyJ...",
  "user": { "id": 1, "nombreCompleto": "...", "email": "...", "rol": "ADMINISTRADOR" }
}
```

---

### Usuarios — `/api/usuarios` (solo ADMINISTRADOR)

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/usuarios` | Listar todos |
| `GET` | `/api/usuarios/odontologos` | Listar odontólogos |
| `GET` | `/api/usuarios/{id}` | Obtener por ID |
| `POST` | `/api/usuarios` | Crear |
| `PUT` | `/api/usuarios/{id}` | Actualizar |
| `DELETE` | `/api/usuarios/{id}` | Eliminar |

---

### Pacientes — `/api/pacientes`

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/pacientes?page=1&size=10` | Listar paginado |
| `GET` | `/api/pacientes/buscar?q=texto` | Buscar por nombre o DNI |
| `GET` | `/api/pacientes/{id}` | Obtener por ID |
| `POST` | `/api/pacientes` | Crear |
| `PUT` | `/api/pacientes/{id}` | Actualizar |
| `DELETE` | `/api/pacientes/{id}` | Eliminar |

```json
// PacienteDto
{
  "id": 1,
  "nombreCompleto": "Ana Torres Ramírez",
  "dni": "12345678",
  "fechaNacimiento": "1990-03-15",
  "telefono": "987654321",
  "email": "ana.torres@gmail.com"
}
```

---

### Historial Clínico — `/api/historial`

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/historial/paciente/{pacienteId}` | Obtener ficha médica |
| `PUT` | `/api/historial/paciente/{pacienteId}` | Crear o actualizar ficha médica |

```json
// HistorialClinicoDto
{
  "id": 1,
  "pacienteId": 1,
  "alergias": "Penicilina",
  "condicionesMedicas": "Ninguna",
  "medicamentosActuales": "Ninguno",
  "grupoSanguineo": "B+",
  "fechaActualizacion": "2026-05-23"
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
| `POST` | `/api/citas` | Crear |
| `PUT` | `/api/citas/{id}` | Actualizar (→ REAGENDADO) |
| `PATCH` | `/api/citas/{id}/cancelar` | Cancelar (→ CANCELADO) |
| `PATCH` | `/api/citas/{id}/estado` | Cambiar estado manualmente |
| `PUT` | `/api/citas/{id}/finalizar` | Marcar atendida (→ ATENDIDO) |

**Estados:** `PENDIENTE` → `ATENDIDO` / `CANCELADO` / `REAGENDADO`

---

### Atenciones — `/api/atenciones` (Admin y Odontólogo)

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/api/atenciones` | Registrar atención (genera reporte automáticamente) |
| `GET` | `/api/atenciones?pacienteId={id}` | Historial de un paciente |
| `GET` | `/api/atenciones/cita/{citaId}` | Atención de una cita |

---

### Catálogo de Tratamientos — `/api/tratamientos` (solo ADMINISTRADOR)

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/tratamientos` | Listar todos |
| `GET` | `/api/tratamientos/{id}` | Obtener por ID |
| `POST` | `/api/tratamientos` | Crear |
| `PUT` | `/api/tratamientos/{id}` | Actualizar |
| `DELETE` | `/api/tratamientos/{id}` | Eliminar |

```json
// TratamientoCatalogoDto
{
  "id": 1,
  "nombre": "Limpieza dental",
  "descripcion": "Eliminación de sarro y placa bacteriana",
  "precio": 80.0,
  "duracionMinutos": 45
}
```

---

### Pagos — `/api/pagos` (Admin y Recepcionista)

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/pagos` | Listar todos |
| `GET` | `/api/pagos/{id}` | Obtener por ID |
| `GET` | `/api/pagos/atencion/{atencionId}` | Pago de una atención |
| `POST` | `/api/pagos` | Registrar pago |
| `PATCH` | `/api/pagos/{id}/estado` | Cambiar estado (`PENDIENTE` → `PAGADO` / `ANULADO`) |

```json
// PagoDto
{
  "id": 1,
  "atencionId": 1,
  "pacienteId": 1,
  "pacienteNombre": "Ana Torres Ramírez",
  "odontologoNombre": "Dr. Carlos Mendoza",
  "citaFecha": "2026-05-01",
  "monto": 80.0,
  "metodoPago": "EFECTIVO",
  "fechaPago": "2026-05-02",
  "estado": "PAGADO"
}
```

**Estados de pago:** `PENDIENTE` → `PAGADO` / `ANULADO`
**Métodos de pago:** `EFECTIVO` / `TARJETA` / `TRANSFERENCIA`

---

### Reportes — `/api/reportes` (Admin y Odontólogo)

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/reportes?page=1&size=10` | Listar paginados |
| `GET` | `/api/reportes/{id}` | Obtener por ID |
| `GET` | `/api/reportes/cita/{citaId}` | Reporte de una cita |
| `POST` | `/api/reportes/generar/{citaId}` | Generar desde cita |
| `GET` | `/api/reportes/{id}/pdf` | Descargar PDF |

---

## Estructura del proyecto

```
src/main/java/com/dentalpro/
├── auth/               # Login, registro y recuperación de contraseña
├── usuario/            # Gestión de usuarios del personal
├── paciente/           # Gestión de pacientes
├── historial/          # Historial clínico por paciente
├── cita/               # Gestión de citas
├── atencion/           # Registros de atención clínica
├── reporte/            # Generación de reportes PDF
├── tratamiento/        # Catálogo de tratamientos y precios
├── pago/               # Registro de pagos por atención
└── config/
    ├── SecurityConfig.java    # Spring Security y CORS
    ├── JwtUtil.java           # Generación y validación de tokens JWT
    ├── JwtAuthFilter.java     # Filtro HTTP para autenticación JWT
    ├── DataInitializer.java   # Seeder de datos por defecto
    └── EmailService.java      # Envío de correos (recuperación contraseña)
```

---

## Modelo de datos

```
Usuario ──────────────────────────────┐
  id, nombreCompleto, email,          │ odontologo_id
  password, rol, activo               │
                                      ↓
Paciente ──────────────────────── Cita ──────────── Atencion ──── Reporte
  id, nombreCompleto, dni,         id, fecha, hora,   id, citaId,    id, citaId,
  fechaNacimiento,                 motivo, estado,    diagnostico,   diagnostico,
  telefono, email                  recordatorio       tratamiento,   tratamiento,
     │                                  │             observaciones  fecha
     │                                  │                 │
     ↓                                  └──── Pago        │
HistorialClinico                          id, atencionId, └── TratamientoCatalogo
  id, pacienteId,                         monto,           id, nombre, descripcion,
  alergias,                               metodoPago,      precio, duracionMinutos
  condicionesMedicas,                     fechaPago,
  medicamentosActuales,                   estado
  grupoSanguineo,
  fechaActualizacion
```

**Total de tablas: 8**
`usuarios` · `pacientes` · `historial_clinico` · `citas` · `atenciones` · `reportes` · `tratamiento_catalogo` · `pagos`

---

## Códigos de respuesta HTTP

| Código | Descripción |
|---|---|
| `200 OK` | Petición exitosa |
| `201 Created` | Recurso creado |
| `204 No Content` | Operación exitosa sin cuerpo |
| `400 Bad Request` | Datos de entrada inválidos |
| `401 Unauthorized` | Token ausente, inválido o expirado |
| `403 Forbidden` | Sin permisos para el recurso |
| `404 Not Found` | Recurso no encontrado |
| `409 Conflict` | Conflicto (DNI o email duplicado) |
| `500 Internal Server Error` | Error interno del servidor |

---

## Autoría

Proyecto diseñado, desarrollado e implementado por:

- **Desarrolladora:** Sheila JPM
- **LinkedIn:** [Sheila Jacqueline Principe Merino](https://www.linkedin.com/in/sheila-jacqueline-principe-merino-1579802aa/)
- **GitHub:** [@SHEILAJPM](https://github.com/SHEILAJPM)
- **Contacto:** [principemerinosheila@gmail.com](mailto:principemerinosheila@gmail.com)

---

*Proyecto desarrollado con fines académicos — Ingeniería de Sistemas e Informática.*
