package com.bititech.ecommerce_service.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.bititech.ecommerce_service.dto.customer.CustomerDto;
import com.bititech.ecommerce_service.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
  private final CustomerService customers;

  @GetMapping
  @PreAuthorize("hasAuthority('PERM_ORDER_READ')")
  public Page<CustomerDto> list(@RequestParam(defaultValue="0") int page,
                                @RequestParam(defaultValue="50") int size) {
    return customers.list(PageRequest.of(page, size, Sort.by("id").descending()));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('PERM_ORDER_READ')")
  public CustomerDto get(@PathVariable Long id) { return customers.get(id); }

  @PostMapping
  @PreAuthorize("hasAuthority('PERM_ORDER_WRITE')")
  public CustomerDto create(@RequestParam @Email @NotBlank String email,
                            @RequestParam @NotBlank String fullName,
                            @RequestParam(required=false) String phone,
                            @RequestParam(required=false) String shippingAddress) {
    return customers.create(email, fullName, phone, shippingAddress);
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasAuthority('PERM_ORDER_WRITE')")
  public CustomerDto update(@PathVariable Long id,
                            @RequestParam String fullName,
                            @RequestParam(required=false) String phone,
                            @RequestParam(required=false) String shippingAddress) {
    return customers.update(id, fullName, phone, shippingAddress);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('PERM_ORDER_WRITE')")
  public void delete(@PathVariable Long id) { customers.delete(id); }
}
