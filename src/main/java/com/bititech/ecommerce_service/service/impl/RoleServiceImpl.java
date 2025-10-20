package com.bititech.ecommerce_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bititech.ecommerce_service.domain.Permission;
import com.bititech.ecommerce_service.domain.Role;
import com.bititech.ecommerce_service.dto.role.RoleDto;
import com.bititech.ecommerce_service.exception.NotFoundException;
import com.bititech.ecommerce_service.mapper.RoleMapper;
import com.bititech.ecommerce_service.repository.PermissionRepository;
import com.bititech.ecommerce_service.repository.RoleRepository;
import com.bititech.ecommerce_service.service.RoleService;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roles;
  private final PermissionRepository perms;
  private final RoleMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public Page<RoleDto> list(Pageable pageable) {
    return roles.findAll(pageable).map(mapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public RoleDto get(Long id) {
    Role r = roles.findById(id).orElseThrow(() -> new NotFoundException("Role not found"));
    return mapper.toDto(r);
  }

  @Override
  public RoleDto create(String code, String name, Set<String> permissionCodes) {
    Role r = Role.builder().code(code).name(name).build();
    if (permissionCodes != null && !permissionCodes.isEmpty()) {
      Set<Permission> pset = new HashSet<>();
      for (String c : permissionCodes) {
        Permission p = perms.findByCode(c)
            .orElseThrow(() -> new NotFoundException("Permission not found: " + c));
        pset.add(p);
      }
      r.setPermissions(pset);
    }
    return mapper.toDto(roles.save(r));
  }

  @Override
  public RoleDto updateName(Long id, String name) {
    Role r = roles.findById(id).orElseThrow(() -> new NotFoundException("Role not found"));
    r.setName(name);
    return mapper.toDto(r);
  }

  @Override
  public RoleDto setPermissions(Long id, Set<String> permissionCodes) {
    Role r = roles.findById(id).orElseThrow(() -> new NotFoundException("Role not found"));
    Set<Permission> pset = new HashSet<>();
    if (permissionCodes != null) {
      for (String c : permissionCodes) {
        Permission p = perms.findByCode(c)
            .orElseThrow(() -> new NotFoundException("Permission not found: " + c));
        pset.add(p);
      }
    }
    r.getPermissions().clear();
    r.getPermissions().addAll(pset);
    return mapper.toDto(r);
  }

  @Override
  public void delete(Long id) {
    roles.deleteById(id);
  }
}
