package com.bititech.ecommerce_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bititech.ecommerce_service.domain.Inventory;
import com.bititech.ecommerce_service.dto.inventory.InventoryDto;
import com.bititech.ecommerce_service.exception.BusinessException;
import com.bititech.ecommerce_service.exception.NotFoundException;
import com.bititech.ecommerce_service.mapper.InventoryMapper;
import com.bititech.ecommerce_service.repository.InventoryRepository;
import com.bititech.ecommerce_service.service.InventoryService;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

  private final InventoryRepository inventoryRepo;
  private final InventoryMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public Page<InventoryDto> list(Pageable pageable) {
    return inventoryRepo.findAll(pageable).map(mapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public InventoryDto get(Long id) {
    Inventory inv = inventoryRepo.findById(id)
        .orElseThrow(() -> new NotFoundException("Inventory not found"));
    return mapper.toDto(inv);
  }

  @Override
  public InventoryDto adjust(Long id, long delta) {
    Inventory inv = inventoryRepo.findById(id)
        .orElseThrow(() -> new NotFoundException("Inventory not found"));
    long next = inv.getQuantity() + delta;
    if (next < 0) {
      throw new BusinessException("Inventory cannot go negative (current=" + inv.getQuantity() + ", delta=" + delta + ")");
    }
    inv.setQuantity(next);
    return mapper.toDto(inv);
  }

  @Override
  public InventoryDto setMinimumThreshold(Long id, long min) {
    Inventory inv = inventoryRepo.findById(id)
        .orElseThrow(() -> new NotFoundException("Inventory not found"));
    inv.setMinimumThreshold(min);
    return mapper.toDto(inv);
  }
}
