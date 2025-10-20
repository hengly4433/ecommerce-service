package com.bititech.ecommerce_service.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.bititech.ecommerce_service.dto.user.UserCreateDto;
import com.bititech.ecommerce_service.dto.user.UserDto;
import com.bititech.ecommerce_service.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService users;

  @GetMapping
  @PreAuthorize("hasAuthority('PERM_USER_READ')")
  public Page<UserDto> list(@RequestParam(defaultValue="0") int page,
                            @RequestParam(defaultValue="20") int size) {
    return users.list(PageRequest.of(page, size, Sort.by("id").descending()));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('PERM_USER_READ')")
  public UserDto get(@PathVariable Long id) { return users.get(id); }

  @PostMapping
  @PreAuthorize("hasAuthority('PERM_USER_WRITE')")
  public UserDto create(@RequestBody @Valid UserCreateDto dto) { return users.create(dto); }

  @PatchMapping("/{id}/enabled")
  @PreAuthorize("hasAuthority('PERM_USER_WRITE')")
  public UserDto enable(@PathVariable Long id, @RequestParam boolean value) { return users.enable(id, value); }

  @PatchMapping("/{id}/locked")
  @PreAuthorize("hasAuthority('PERM_USER_WRITE')")
  public UserDto lock(@PathVariable Long id, @RequestParam boolean value) { return users.lock(id, value); }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('PERM_USER_WRITE')")
  public void delete(@PathVariable Long id) { users.delete(id); }
}
