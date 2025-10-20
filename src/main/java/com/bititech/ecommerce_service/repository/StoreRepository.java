package com.bititech.ecommerce_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bititech.ecommerce_service.domain.Store;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
  Optional<Store> findByCode(String code);
}

