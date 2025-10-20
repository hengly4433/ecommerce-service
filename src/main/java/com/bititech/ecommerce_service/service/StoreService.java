package com.bititech.ecommerce_service.service;

import org.springframework.data.domain.*;

import com.bititech.ecommerce_service.dto.store.StoreDto;

public interface StoreService {
  Page<StoreDto> list(Pageable pageable);
  StoreDto get(Long id);
  StoreDto create(String code, String name, String addressLine, String city, String country);
  StoreDto rename(Long id, String name);
  void delete(Long id);
}
