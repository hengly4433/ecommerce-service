package com.bititech.ecommerce_service.dto.purchase;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public record PurchaseCreateDto(
    @NotBlank String supplierName,
    String referenceNo,
    String note,
    @NotEmpty List<Item> items
) {
  public record Item(@NotNull Long productId, @NotNull Long quantity, @NotNull BigDecimal unitCost) {}
}
