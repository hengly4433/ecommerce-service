package com.bititech.ecommerce_service.web;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.bititech.ecommerce_service.dto.permission.PermissionDto;
import com.bititech.ecommerce_service.service.PermissionService;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {
  private final PermissionService perms;

  @GetMapping
  @PreAuthorize("hasAuthority('PERM_ROLE_READ') or hasAuthority('PERM_USER_READ')")
  public Page<PermissionDto> list(@RequestParam(defaultValue="0") int page,
                                  @RequestParam(defaultValue="50") int size) {
    return perms.list(PageRequest.of(page, size, Sort.by("id").ascending()));
  }

  @PostMapping
  @PreAuthorize("hasAuthority('PERM_ROLE_WRITE')")
  public PermissionDto create(@RequestParam @NotBlank String code,
                              @RequestParam @NotBlank String name) {
    return perms.create(code, name);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('PERM_ROLE_WRITE')")
  public void delete(@PathVariable Long id) { perms.delete(id); }
}
