package com.bititech.ecommerce_service.repository;

import org.springframework.data.domain.*;

import com.bititech.ecommerce_service.domain.Product;

public interface ProductRepositoryCustom {
  Page<Product> search(String q, Long categoryId, Pageable pageable);
}
