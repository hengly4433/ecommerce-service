package com.bititech.ecommerce_service.service;

import org.springframework.data.domain.*;

import com.bititech.ecommerce_service.dto.role.RoleDto;

import java.util.Set;

public interface RoleService {
  Page<RoleDto> list(Pageable pageable);
  RoleDto get(Long id);
  RoleDto create(String code, String name, Set<String> permissionCodes);
  RoleDto updateName(Long id, String name);
  RoleDto setPermissions(Long id, Set<String> permissionCodes);
  void delete(Long id);
}
