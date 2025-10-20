package com.bititech.ecommerce_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bititech.ecommerce_service.domain.Category;
import com.bititech.ecommerce_service.dto.category.CategoryDto;
import com.bititech.ecommerce_service.exception.NotFoundException;
import com.bititech.ecommerce_service.mapper.CategoryMapper;
import com.bititech.ecommerce_service.repository.CategoryRepository;
import com.bititech.ecommerce_service.service.CategoryService;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categories;
  private final CategoryMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public Page<CategoryDto> list(Pageable pageable) {
    return categories.findAll(pageable).map(mapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public CategoryDto get(Long id) {
    Category c = categories.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
    return mapper.toDto(c);
  }

  @Override
  public CategoryDto create(Long parentId, String name, String slug) {
    Category parent = null;
    if (parentId != null) {
      parent = categories.findById(parentId)
          .orElseThrow(() -> new NotFoundException("Parent category not found: " + parentId));
    }
    Category c = Category.builder()
        .parent(parent)
        .name(name)
        .slug(slug)
        .build();
    return mapper.toDto(categories.save(c));
  }

  @Override
  public CategoryDto rename(Long id, String name) {
    Category c = categories.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
    c.setName(name);
    return mapper.toDto(c);
  }

  @Override
  public void delete(Long id) {
    categories.deleteById(id);
  }
}
