package com.bititech.ecommerce_service.dto.user;

import jakarta.validation.constraints.*;

import java.util.Set;

public record UserCreateDto(
    @Email @NotBlank String email,
    @NotBlank @Size(min=6) String password,
    @NotBlank String fullName,
    Set<String> roleCodes
) {}
