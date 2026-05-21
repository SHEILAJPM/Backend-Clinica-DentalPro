package com.dentalpro.atencion.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AtencionDto(
        Long id,
        @NotNull Long citaId,
        String diagnostico,
        String tratamiento,
        String observaciones,
        LocalDate fecha
) {}
