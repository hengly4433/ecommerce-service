package com.bititech.ecommerce_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.bititech.ecommerce_service.domain.Purchase;
import com.bititech.ecommerce_service.dto.purchase.PurchaseDto;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {Collectors.class})
public interface PurchaseMapper {

  @Mapping(
      target = "items",
      expression =
          "java(purchase.getItems().stream()"
          + ".map(i -> new PurchaseDto.Item("
          + "  i.getId(),"
          + "  i.getProduct().getId(),"
          + "  i.getQuantity(),"
          + "  i.getUnitCost(),"
          + "  i.getLineTotal()"
          + "))"
          + ".collect(Collectors.toList()))"
  )
  PurchaseDto toDto(Purchase purchase);
}
