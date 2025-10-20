package com.bititech.ecommerce_service.web;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.bititech.ecommerce_service.dto.store.StoreDto;
import com.bititech.ecommerce_service.service.StoreService;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {
  private final StoreService stores;

  @GetMapping
  @PreAuthorize("permitAll() or hasAuthority('PERM_PRODUCT_READ')")
  public Page<StoreDto> list(@RequestParam(defaultValue="0") int page,
                             @RequestParam(defaultValue="50") int size) {
    return stores.list(PageRequest.of(page, size, Sort.by("id").descending()));
  }

  @GetMapping("/{id}")
  @PreAuthorize("permitAll() or hasAuthority('PERM_PRODUCT_READ')")
  public StoreDto get(@PathVariable Long id) { return stores.get(id); }

  @PostMapping
  @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
  public StoreDto create(@RequestParam @NotBlank String code,
                         @RequestParam @NotBlank String name,
                         @RequestParam(required=false) String addressLine,
                         @RequestParam(required=false) String city,
                         @RequestParam(required=false) String country) {
    return stores.create(code, name, addressLine, city, country);
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
  public StoreDto update(@PathVariable Long id,
                         @RequestParam String name) {
    return stores.rename(id, name);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
  public void delete(@PathVariable Long id) { stores.delete(id); }
}
