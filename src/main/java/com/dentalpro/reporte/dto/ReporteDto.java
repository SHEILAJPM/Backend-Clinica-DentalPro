package com.dentalpro.reporte.dto;

import java.time.LocalDate;

public record ReporteDto(
        Long id,
        Long citaId,
        String pacienteNombre,
        String odontologoNombre,
        String diagnostico,
        String tratamiento,
        String observaciones,
        LocalDate fecha
) {}
