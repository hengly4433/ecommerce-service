package com.bititech.ecommerce_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.bititech.ecommerce_service.domain.Product;
import com.bititech.ecommerce_service.dto.product.ProductDto;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  @Mapping(target = "categoryId", source = "category.id")
  @Mapping(target = "storeId", source = "store.id")
  ProductDto toDto(Product entity);
}
