package com.bititech.ecommerce_service.dto.product;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductCreateDto(
    @NotBlank String sku,
    @NotBlank String name,
    String description,
    @NotNull @DecimalMin("0.00") BigDecimal price,
    Long categoryId,
    Long storeId
) {}
