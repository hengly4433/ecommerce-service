package com.bititech.ecommerce_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bititech.ecommerce_service.domain.Category;
import com.bititech.ecommerce_service.domain.Inventory;
import com.bititech.ecommerce_service.domain.Product;
import com.bititech.ecommerce_service.domain.Store;
import com.bititech.ecommerce_service.dto.product.ProductCreateDto;
import com.bititech.ecommerce_service.dto.product.ProductDto;
import com.bititech.ecommerce_service.exception.NotFoundException;
import com.bititech.ecommerce_service.mapper.ProductMapper;
import com.bititech.ecommerce_service.repository.CategoryRepository;
import com.bititech.ecommerce_service.repository.InventoryRepository;
import com.bititech.ecommerce_service.repository.ProductRepository;
import com.bititech.ecommerce_service.repository.StoreRepository;
import com.bititech.ecommerce_service.service.ProductService;
import com.bititech.ecommerce_service.storage.StorageService;

import java.math.BigDecimal;
import java.util.*;

@Service @RequiredArgsConstructor @Transactional
public class ProductServiceImpl implements ProductService {
  private final ProductRepository products;
  private final CategoryRepository categories;
  private final StoreRepository stores;
  private final InventoryRepository inventoryRepo;
  private final ProductMapper mapper;
  private final StorageService storage;

  @Override
  @Transactional(readOnly = true)
  public Page<ProductDto> search(String q, Long categoryId, Pageable pageable) {
    return products.search(q, categoryId, pageable).map(mapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public ProductDto get(Long id) {
    var p = products.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
    return mapper.toDto(p);
  }

  @Override
  public ProductDto create(ProductCreateDto dto) {
    Category c = null;
    if (dto.categoryId() != null) c = categories.findById(dto.categoryId()).orElse(null);
    Store s = null;
    if (dto.storeId() != null) s = stores.findById(dto.storeId()).orElse(null);

    Product p = Product.builder()
        .sku(dto.sku())
        .name(dto.name())
        .description(dto.description())
        .price(dto.price())
        .category(c)
        .store(s)
        .imageUrls(new ArrayList<>())
        .build();
    p = products.save(p);

    // init inventory
    var inv = Inventory.builder().product(p).quantity(0).minimumThreshold(0).build();
    inventoryRepo.save(inv);

    return mapper.toDto(p);
  }

  @Override
  public ProductDto setMainImage(Long productId, MultipartFile file) {
    var p = products.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));
    String url = storage.uploadProductMainImage(p.getSku(), file);
    p.setMainImageUrl(url);
    return mapper.toDto(p);
  }

  @Override
  public ProductDto addGallery(Long productId, List<MultipartFile> files) {
    var p = products.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));
    List<String> urls = storage.uploadProductGallery(p.getSku(), files);
    var current = new ArrayList<>(Optional.ofNullable(p.getImageUrls()).orElseGet(ArrayList::new));
    current.addAll(urls);
    p.setImageUrls(current);
    return mapper.toDto(p);
  }

  @Override
  public ProductDto updatePrice(Long productId, String newPrice) {
    var p = products.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));
    p.setPrice(new BigDecimal(newPrice));
    return mapper.toDto(p);
  }

  @Override
  public void delete(Long id) {
    products.deleteById(id);
  }
}


