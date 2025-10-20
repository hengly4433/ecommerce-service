package com.bititech.ecommerce_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bititech.ecommerce_service.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> { }
