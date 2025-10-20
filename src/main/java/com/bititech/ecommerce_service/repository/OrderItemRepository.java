package com.bititech.ecommerce_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bititech.ecommerce_service.domain.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> { }
