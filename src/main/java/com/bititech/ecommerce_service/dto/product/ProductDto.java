package com.bititech.ecommerce_service.dto.product;

import java.math.BigDecimal;
import java.util.List;

public record ProductDto(
    Long id,
    String sku,
    String name,
    String description,
    BigDecimal price,
    String mainImageUrl,
    List<String> imageUrls,
    Long categoryId,
    Long storeId
) {}

