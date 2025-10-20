package com.bititech.ecommerce_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bititech.ecommerce_service.domain.Purchase;
import com.bititech.ecommerce_service.domain.PurchaseItem;
import com.bititech.ecommerce_service.dto.purchase.PurchaseCreateDto;
import com.bititech.ecommerce_service.dto.purchase.PurchaseDto;
import com.bititech.ecommerce_service.exception.NotFoundException;
import com.bititech.ecommerce_service.mapper.PurchaseMapper;
import com.bititech.ecommerce_service.repository.InventoryRepository;
import com.bititech.ecommerce_service.repository.ProductRepository;
import com.bititech.ecommerce_service.repository.PurchaseItemRepository;
import com.bititech.ecommerce_service.repository.PurchaseRepository;
import com.bititech.ecommerce_service.service.PurchaseService;

import java.math.BigDecimal;

@Service @RequiredArgsConstructor @Transactional
public class PurchaseServiceImpl implements PurchaseService {
  private final PurchaseRepository purchases;
  private final PurchaseItemRepository purchaseItems;
  private final ProductRepository products;
  private final InventoryRepository inventoryRepo;
  private final PurchaseMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public Page<PurchaseDto> list(Pageable pageable) {
    return purchases.findAll(pageable).map(mapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public PurchaseDto get(Long id) {
    var p = purchases.findById(id).orElseThrow(() -> new NotFoundException("Purchase not found"));
    return mapper.toDto(p);
  }

  @Override
  public PurchaseDto create(PurchaseCreateDto dto) {
    Purchase p = Purchase.builder()
        .supplierName(dto.supplierName())
        .referenceNo(dto.referenceNo())
        .note(dto.note())
        .totalAmount(BigDecimal.ZERO)
        .build();
    p = purchases.save(p);

    BigDecimal total = BigDecimal.ZERO;
    for (var it : dto.items()) {
      var prod = products.findById(it.productId()).orElseThrow(() -> new NotFoundException("Product not found: "+it.productId()));
      var line = it.unitCost().multiply(BigDecimal.valueOf(it.quantity()));
      total = total.add(line);
      var pi = PurchaseItem.builder()
          .purchase(p).product(prod)
          .unitCost(it.unitCost())
          .quantity(it.quantity())
          .lineTotal(line)
          .build();
      purchaseItems.save(pi);
      p.getItems().add(pi);

      // increase inventory
      var inv = inventoryRepo.findByProduct(prod).orElseThrow();
      inv.setQuantity(inv.getQuantity() + it.quantity());
    }
    p.setTotalAmount(total);
    return mapper.toDto(p);
  }

  @Override
  public void delete(Long id) {
    purchases.deleteById(id);
  }
}
