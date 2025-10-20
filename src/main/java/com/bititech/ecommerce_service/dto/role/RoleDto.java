package com.bititech.ecommerce_service.dto.role;

import java.util.Set;

public record RoleDto(Long id, String code, String name, Set<String> permissions) {}
