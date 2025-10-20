package com.bititech.ecommerce_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bititech.ecommerce_service.domain.Permission;
import com.bititech.ecommerce_service.dto.permission.PermissionDto;
import com.bititech.ecommerce_service.mapper.PermissionMapper;
import com.bititech.ecommerce_service.repository.PermissionRepository;
import com.bititech.ecommerce_service.service.PermissionService;

@Service
@RequiredArgsConstructor
@Transactional
public class PermissionServiceImpl implements PermissionService {

  private final PermissionRepository perms;
  private final PermissionMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public Page<PermissionDto> list(Pageable pageable) {
    return perms.findAll(pageable).map(mapper::toDto);
  }

  @Override
  public PermissionDto create(String code, String name) {
    Permission p = Permission.builder().code(code).name(name).build();
    return mapper.toDto(perms.save(p));
  }

  @Override
  public void delete(Long id) {
    perms.deleteById(id);
  }
}
