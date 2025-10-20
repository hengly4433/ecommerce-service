package com.bititech.ecommerce_service.dto.order;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public record OrderCreateDto(
    @NotNull Long customerId,
    Long storeId,
    @NotNull @DecimalMin("0.00") BigDecimal discount,
    @NotNull @DecimalMin("0.00") BigDecimal tax,
    @NotEmpty List<OrderItemCreate> items
) {
  public record OrderItemCreate(@NotNull Long productId, @NotNull Long quantity, @NotNull BigDecimal unitPrice) {}
}

