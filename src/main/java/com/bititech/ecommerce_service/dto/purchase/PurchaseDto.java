package com.bititech.ecommerce_service.dto.purchase;

import java.math.BigDecimal;
import java.util.List;

public record PurchaseDto(
    Long id, String supplierName, String referenceNo, String note, BigDecimal totalAmount, List<Item> items
) {
  public record Item(Long id, Long productId, Long quantity, BigDecimal unitCost, BigDecimal lineTotal) {}
}
