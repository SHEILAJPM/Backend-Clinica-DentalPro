package com.dentalpro.auth.dto;

import jakarta.validation.constraints.*;

public record VerifyCodeRequest(
        @Email @NotBlank String email,
        @NotBlank @Size(min = 6, max = 6) String code
) {}
