package com.bititech.ecommerce_service.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.bititech.ecommerce_service.dto.purchase.PurchaseCreateDto;
import com.bititech.ecommerce_service.dto.purchase.PurchaseDto;
import com.bititech.ecommerce_service.service.PurchaseService;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {
  private final PurchaseService purchases;

  @GetMapping
  @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
  public Page<PurchaseDto> list(@RequestParam(defaultValue="0") int page,
                                @RequestParam(defaultValue="20") int size) {
    return purchases.list(PageRequest.of(page, size, Sort.by("id").descending()));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
  public PurchaseDto get(@PathVariable Long id) { return purchases.get(id); }

  @PostMapping
  @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
  public PurchaseDto create(@RequestBody @Valid PurchaseCreateDto dto) { return purchases.create(dto); }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
  public void delete(@PathVariable Long id) { purchases.delete(id); }
}
