package com.bititech.ecommerce_service.service;

import org.springframework.data.domain.*;

import com.bititech.ecommerce_service.dto.customer.CustomerDto;

public interface CustomerService {
  Page<CustomerDto> list(Pageable pageable);
  CustomerDto get(Long id);
  CustomerDto create(String email, String fullName, String phone, String shippingAddress);
  CustomerDto update(Long id, String fullName, String phone, String shippingAddress);
  void delete(Long id);
}
