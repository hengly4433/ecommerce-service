package com.bititech.ecommerce_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bititech.ecommerce_service.domain.Inventory;
import com.bititech.ecommerce_service.domain.Product;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
  Optional<Inventory> findByProduct(Product p);
}

