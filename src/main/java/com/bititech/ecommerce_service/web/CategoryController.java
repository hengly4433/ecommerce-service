package com.bititech.ecommerce_service.web;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.bititech.ecommerce_service.dto.category.CategoryDto;
import com.bititech.ecommerce_service.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
  private final CategoryService categories;

  @GetMapping
  @PreAuthorize("permitAll() or hasAuthority('PERM_PRODUCT_READ')")
  public Page<CategoryDto> list(@RequestParam(defaultValue="0") int page,
                                @RequestParam(defaultValue="50") int size) {
    return categories.list(PageRequest.of(page, size, Sort.by("id").descending()));
  }

  @GetMapping("/{id}")
  @PreAuthorize("permitAll() or hasAuthority('PERM_PRODUCT_READ')")
  public CategoryDto get(@PathVariable Long id) { return categories.get(id); }

  @PostMapping
  @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
  public CategoryDto create(@RequestParam(required=false) Long parentId,
                            @RequestParam @NotBlank String name,
                            @RequestParam @NotBlank String slug) {
    return categories.create(parentId, name, slug);
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
  public CategoryDto rename(@PathVariable Long id, @RequestParam String name) {
    return categories.rename(id, name);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
  public void delete(@PathVariable Long id) { categories.delete(id); }
}
