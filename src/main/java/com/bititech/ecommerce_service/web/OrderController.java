package com.bititech.ecommerce_service.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.bititech.ecommerce_service.dto.order.OrderCreateDto;
import com.bititech.ecommerce_service.dto.order.OrderDto;
import com.bititech.ecommerce_service.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
  private final OrderService orders;

  @GetMapping
  @PreAuthorize("hasAuthority('PERM_ORDER_READ')")
  public Page<OrderDto> list(@RequestParam(defaultValue="0") int page,
                             @RequestParam(defaultValue="20") int size) {
    return orders.list(PageRequest.of(page, size, Sort.by("id").descending()));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('PERM_ORDER_READ')")
  public OrderDto get(@PathVariable Long id) { return orders.get(id); }

  @PostMapping
  @PreAuthorize("hasAuthority('PERM_ORDER_WRITE')")
  public OrderDto create(@RequestBody @Valid OrderCreateDto dto) { return orders.create(dto); }

  @PostMapping("/{id}/paid")
  @PreAuthorize("hasAuthority('PERM_ORDER_WRITE')")
  public OrderDto markPaid(@PathVariable Long id) { return orders.markPaid(id); }

  @PatchMapping("/{id}/status")
  @PreAuthorize("hasAuthority('PERM_ORDER_WRITE')")
  public OrderDto updateStatus(@PathVariable Long id, @RequestParam String status) { return orders.updateStatus(id, status); }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('PERM_ORDER_WRITE')")
  public void delete(@PathVariable Long id) { orders.delete(id); }
}
