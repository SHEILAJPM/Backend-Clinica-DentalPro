package com.dentalpro.auth.dto;

import jakarta.validation.constraints.*;

public record ResetPasswordRequest(
        @Email @NotBlank String email,
        @NotBlank @Size(min = 6, max = 6) String code,
        @NotBlank @Size(min = 6) String newPassword
) {}
