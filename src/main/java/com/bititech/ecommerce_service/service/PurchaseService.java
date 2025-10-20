package com.bititech.ecommerce_service.service;

import org.springframework.data.domain.*;

import com.bititech.ecommerce_service.dto.purchase.PurchaseCreateDto;
import com.bititech.ecommerce_service.dto.purchase.PurchaseDto;

public interface PurchaseService {
  Page<PurchaseDto> list(Pageable pageable);
  PurchaseDto get(Long id);
  PurchaseDto create(PurchaseCreateDto dto);
  void delete(Long id);
}
