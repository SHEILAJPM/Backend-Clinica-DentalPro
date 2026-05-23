package com.dentalpro.pago.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record PagoDto(
        Long id,
        @NotNull Long atencionId,
        Long pacienteId,
        String pacienteNombre,
        String odontologoNombre,
        LocalDate citaFecha,
        @NotNull Double monto,
        @NotNull String metodoPago,
        LocalDate fechaPago,
        String estado
) {}
