package com.bititech.ecommerce_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bititech.ecommerce_service.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> { }

