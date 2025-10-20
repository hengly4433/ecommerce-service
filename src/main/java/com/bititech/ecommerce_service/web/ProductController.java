package com.bititech.ecommerce_service.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.bititech.ecommerce_service.dto.product.ProductCreateDto;
import com.bititech.ecommerce_service.dto.product.ProductDto;
import com.bititech.ecommerce_service.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
  private final ProductService products;

  @GetMapping
  // read for catalog is public in base SecurityConfig (GET permitted) — but we also allow auth read
  @PreAuthorize("permitAll() or hasAuthority('PERM_PRODUCT_READ')")
  public Page<ProductDto> search(@RequestParam(required=false) String q,
                                 @RequestParam(required=false) Long categoryId,
                                 @RequestParam(defaultValue="0") int page,
                                 @RequestParam(defaultValue="20") int size) {
    return products.search(q, categoryId, PageRequest.of(page, size, Sort.by("id").descending()));
  }

  @GetMapping("/{id}")
  @PreAuthorize("permitAll() or hasAuthority('PERM_PRODUCT_READ')")
  public ProductDto get(@PathVariable Long id) { return products.get(id); }

  @PostMapping
  @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
  public ProductDto create(@RequestBody @Valid ProductCreateDto dto) { return products.create(dto); }

  @PostMapping(value="/{id}/main-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
  public ProductDto setMainImage(@PathVariable Long id, @RequestPart("file") MultipartFile file) {
    return products.setMainImage(id, file);
  }

  @PostMapping(value="/{id}/gallery", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
  public ProductDto addGallery(@PathVariable Long id, @RequestPart("files") List<MultipartFile> files) {
    return products.addGallery(id, files);
  }

  @PatchMapping("/{id}/price")
  @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
  public ProductDto updatePrice(@PathVariable Long id, @RequestParam String value) { return products.updatePrice(id, value); }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
  public void delete(@PathVariable Long id) { products.delete(id); }
}
