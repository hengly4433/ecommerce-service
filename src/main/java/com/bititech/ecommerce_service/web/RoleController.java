package com.bititech.ecommerce_service.web;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.bititech.ecommerce_service.dto.role.RoleDto;
import com.bititech.ecommerce_service.service.RoleService;

import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
  private final RoleService roles;

  @GetMapping
  @PreAuthorize("hasAuthority('PERM_ROLE_READ')")
  public Page<RoleDto> list(@RequestParam(defaultValue="0") int page,
                            @RequestParam(defaultValue="20") int size) {
    return roles.list(PageRequest.of(page, size, Sort.by("id").descending()));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('PERM_ROLE_READ')")
  public RoleDto get(@PathVariable Long id) { return roles.get(id); }

  @PostMapping
  @PreAuthorize("hasAuthority('PERM_ROLE_WRITE')")
  public RoleDto create(@RequestParam @NotBlank String code,
                        @RequestParam @NotBlank String name,
                        @RequestParam(required=false) Set<String> permissionCodes) {
    return roles.create(code, name, permissionCodes);
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasAuthority('PERM_ROLE_WRITE')")
  public RoleDto update(@PathVariable Long id,
                        @RequestParam String name) {
    return roles.updateName(id, name);
  }

  @PutMapping("/{id}/permissions")
  @PreAuthorize("hasAuthority('PERM_ROLE_WRITE')")
  public RoleDto setPermissions(@PathVariable Long id,
                                @RequestParam Set<String> permissionCodes) {
    return roles.setPermissions(id, permissionCodes);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('PERM_ROLE_WRITE')")
  public void delete(@PathVariable Long id) { roles.delete(id); }
}
