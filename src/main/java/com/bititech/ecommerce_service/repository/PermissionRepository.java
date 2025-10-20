package com.bititech.ecommerce_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bititech.ecommerce_service.domain.Permission;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
  Optional<Permission> findByCode(String code);
}

