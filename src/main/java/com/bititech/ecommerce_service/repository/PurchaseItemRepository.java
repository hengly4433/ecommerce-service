package com.bititech.ecommerce_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bititech.ecommerce_service.domain.PurchaseItem;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> { }
