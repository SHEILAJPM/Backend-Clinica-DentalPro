package com.dentalpro.paciente.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PacienteDto(
        Long id,
        @NotBlank String nombreCompleto,
        @NotBlank @Size(min = 8, max = 8) String dni,
        LocalDate fechaNacimiento,
        String telefono,
        String email
) {}
