package com.dentalpro.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioDto(
        Long id,
        @NotBlank String nombreCompleto,
        @Email @NotBlank String email,
        String password,
        @NotNull String rol,
        Boolean activo
) {}
