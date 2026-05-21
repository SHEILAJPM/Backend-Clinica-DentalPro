package com.dentalpro.auth.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record RegistroPacienteRequest(
        @NotBlank String nombreCompleto,
        @NotBlank @Size(min = 8, max = 8) String dni,
        @NotNull LocalDate fechaNacimiento,
        @NotBlank String telefono,
        @Email @NotBlank String email,
        @NotBlank @Size(min = 6) String password
) {}
