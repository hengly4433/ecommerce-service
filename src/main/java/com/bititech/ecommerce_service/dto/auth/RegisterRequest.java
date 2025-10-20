package com.bititech.ecommerce_service.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * Public signup payload.
 * - email: required, valid format
 * - password: required, min length 6
 * - fullName: required
 * - roleCodes: optional (e.g., ["CUSTOMER"]); if omitted, service defaults to CUSTOMER
 */
public record RegisterRequest(
    @Email @NotBlank String email,
    @NotBlank @Size(min = 6) String password,
    @NotBlank String fullName,
    Set<String> roleCodes
) {}
