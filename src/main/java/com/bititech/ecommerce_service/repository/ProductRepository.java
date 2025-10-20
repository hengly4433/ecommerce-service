package com.bititech.ecommerce_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.bititech.ecommerce_service.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {}

