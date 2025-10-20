package com.bititech.ecommerce_service.service;

import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;

import com.bititech.ecommerce_service.dto.product.ProductCreateDto;
import com.bititech.ecommerce_service.dto.product.ProductDto;

import java.util.List;

public interface ProductService {
  Page<ProductDto> search(String q, Long categoryId, Pageable pageable);
  ProductDto get(Long id);
  ProductDto create(ProductCreateDto dto);
  ProductDto setMainImage(Long productId, MultipartFile file);
  ProductDto addGallery(Long productId, List<MultipartFile> files);
  ProductDto updatePrice(Long productId, String newPrice);
  void delete(Long id);
}
