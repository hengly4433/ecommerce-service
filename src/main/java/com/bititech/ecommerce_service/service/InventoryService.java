package com.bititech.ecommerce_service.service;

import org.springframework.data.domain.*;

import com.bititech.ecommerce_service.dto.inventory.InventoryDto;

public interface InventoryService {
  Page<InventoryDto> list(Pageable pageable);
  InventoryDto get(Long id);
  InventoryDto adjust(Long id, long delta);
  InventoryDto setMinimumThreshold(Long id, long min);
}
