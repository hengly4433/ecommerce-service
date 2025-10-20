package com.bititech.ecommerce_service.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyEmailRequest(@Email @NotBlank String email) {}
