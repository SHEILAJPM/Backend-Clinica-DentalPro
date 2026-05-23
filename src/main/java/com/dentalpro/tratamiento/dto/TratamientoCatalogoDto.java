package com.dentalpro.tratamiento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TratamientoCatalogoDto(
        Long id,
        @NotBlank String nombre,
        String descripcion,
        @NotNull Double precio,
        @NotNull Integer duracionMinutos
) {}
