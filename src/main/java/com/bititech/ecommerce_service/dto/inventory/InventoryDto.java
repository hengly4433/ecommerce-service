package com.bititech.ecommerce_service.dto.inventory;

public record InventoryDto(Long id, Long productId, long quantity, long minimumThreshold) {}
