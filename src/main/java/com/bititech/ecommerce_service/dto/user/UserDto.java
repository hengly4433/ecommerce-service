package com.bititech.ecommerce_service.dto.user;

import java.util.Set;

public record UserDto(
    Long id,
    String email,
    String fullName,
    boolean enabled,
    boolean locked,
    String profileImageUrl,
    Set<String> roles
) {}
