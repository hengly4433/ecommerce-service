package com.bititech.ecommerce_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.bititech.ecommerce_service.domain.Inventory;
import com.bititech.ecommerce_service.dto.inventory.InventoryDto;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

  @Mapping(target = "productId", source = "product.id")
  InventoryDto toDto(Inventory entity);
}
