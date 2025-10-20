package com.bititech.ecommerce_service.dto.order;

import java.math.BigDecimal;
import java.util.List;

public record OrderDto(
    Long id, String orderNo, String status, Long customerId, Long storeId,
    BigDecimal subtotal, BigDecimal discount, BigDecimal tax, BigDecimal total,
    List<OrderItemDto> items
) {
  public record OrderItemDto(Long id, Long productId, Long quantity, BigDecimal unitPrice, BigDecimal lineTotal) {}
}

