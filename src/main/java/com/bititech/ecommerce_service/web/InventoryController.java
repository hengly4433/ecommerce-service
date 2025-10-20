package com.bititech.ecommerce_service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.bititech.ecommerce_service.dto.inventory.InventoryDto;
import com.bititech.ecommerce_service.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
  private final InventoryService inventory;

  @GetMapping
  @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
  public Page<InventoryDto> list(@RequestParam(defaultValue="0") int page,
                                 @RequestParam(defaultValue="50") int size) {
    return inventory.list(PageRequest.of(page, size, Sort.by("id").descending()));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
  public InventoryDto get(@PathVariable Long id) { return inventory.get(id); }

  @PatchMapping("/{id}/adjust")
  @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
  public InventoryDto adjust(@PathVariable Long id, @RequestParam long delta) {
    return inventory.adjust(id, delta);
  }

  @PatchMapping("/{id}/threshold")
  @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
  public InventoryDto setThreshold(@PathVariable Long id, @RequestParam long min) {
    return inventory.setMinimumThreshold(id, min);
  }
}
