package com.bititech.ecommerce_service.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordResetConfirm(
    @Email @NotBlank String email,
    @NotBlank @Size(min=6, max=6) String code,
    @NotBlank @Size(min=6) String newPassword
) {}
