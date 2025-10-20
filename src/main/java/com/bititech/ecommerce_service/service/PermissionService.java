package com.bititech.ecommerce_service.service;

import org.springframework.data.domain.*;

import com.bititech.ecommerce_service.dto.permission.PermissionDto;

public interface PermissionService {
  Page<PermissionDto> list(Pageable pageable);
  PermissionDto create(String code, String name);
  void delete(Long id);
}
