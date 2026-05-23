package com.dentalpro.cita.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CitaDto(
        Long id,
        @NotNull Long pacienteId,
        String pacienteNombre,
        @NotNull Long odontologoId,
        String odontologoNombre,
        @NotNull LocalDate fecha,
        @NotBlank String hora,
        String motivo,
        String estado,
        Long tratamientoId,
        Double tratamientoPrecio
) {}
