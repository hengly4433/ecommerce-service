package com.bititech.ecommerce_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bititech.ecommerce_service.domain.Customer;
import com.bititech.ecommerce_service.dto.customer.CustomerDto;
import com.bititech.ecommerce_service.exception.NotFoundException;
import com.bititech.ecommerce_service.mapper.CustomerMapper;
import com.bititech.ecommerce_service.repository.CustomerRepository;
import com.bititech.ecommerce_service.service.CustomerService;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customers;
  private final CustomerMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public Page<CustomerDto> list(Pageable pageable) {
    return customers.findAll(pageable).map(mapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public CustomerDto get(Long id) {
    Customer c = customers.findById(id).orElseThrow(() -> new NotFoundException("Customer not found"));
    return mapper.toDto(c);
  }

  @Override
  public CustomerDto create(String email, String fullName, String phone, String shippingAddress) {
    Customer c = Customer.builder()
        .email(email)
        .fullName(fullName)
        .phone(phone)
        .shippingAddress(shippingAddress)
        .build();
    return mapper.toDto(customers.save(c));
  }

  @Override
  public CustomerDto update(Long id, String fullName, String phone, String shippingAddress) {
    Customer c = customers.findById(id).orElseThrow(() -> new NotFoundException("Customer not found"));
    c.setFullName(fullName);
    c.setPhone(phone);
    c.setShippingAddress(shippingAddress);
    return mapper.toDto(c);
  }

  @Override
  public void delete(Long id) {
    customers.deleteById(id);
  }
}
