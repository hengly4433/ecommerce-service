package com.bititech.ecommerce_service.dto.auth;

public record LoginResponse(String token, long expiresInSeconds) {}

