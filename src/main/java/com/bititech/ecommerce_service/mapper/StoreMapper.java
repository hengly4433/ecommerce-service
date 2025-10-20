package com.bititech.ecommerce_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.bititech.ecommerce_service.domain.Store;
import com.bititech.ecommerce_service.dto.store.StoreDto;

@Mapper(componentModel = "spring")
public interface StoreMapper {

  @Mapping(target = "addressLine", source = "address.addressLine")
  @Mapping(target = "city",        source = "address.city")
  @Mapping(target = "country",     source = "address.country")
  StoreDto toDto(Store entity);
}
