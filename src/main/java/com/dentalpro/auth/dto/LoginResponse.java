package com.dentalpro.auth.dto;

public record LoginResponse(
        String token,
        UserInfo user
) {
    public record UserInfo(
            Long id,
            String nombreCompleto,
            String email,
            String rol
    ) {}
}
