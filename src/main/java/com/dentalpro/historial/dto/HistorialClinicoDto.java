package com.dentalpro.historial.dto;

import java.time.LocalDate;

public record HistorialClinicoDto(
        Long id,
        Long pacienteId,
        String alergias,
        String condicionesMedicas,
        String medicamentosActuales,
        String grupoSanguineo,
        LocalDate fechaActualizacion
) {}
