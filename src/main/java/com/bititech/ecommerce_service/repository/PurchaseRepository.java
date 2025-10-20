package com.bititech.ecommerce_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bititech.ecommerce_service.domain.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> { }

