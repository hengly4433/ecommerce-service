package com.bititech.ecommerce_service.service;

import org.springframework.data.domain.*;

import com.bititech.ecommerce_service.dto.category.CategoryDto;

public interface CategoryService {
  Page<CategoryDto> list(Pageable pageable);
  CategoryDto get(Long id);
  CategoryDto create(Long parentId, String name, String slug);
  CategoryDto rename(Long id, String name);
  void delete(Long id);
}
