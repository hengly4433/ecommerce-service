package com.bititech.ecommerce_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.bititech.ecommerce_service.domain.Order;
import com.bititech.ecommerce_service.dto.order.OrderDto;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {Collectors.class})
public interface OrderMapper {

  @Mapping(target = "customerId", source = "customer.id")
  @Mapping(target = "storeId", source = "store.id")
  @Mapping(target = "status", expression = "java(order.getStatus().name())")
  @Mapping(
      target = "items",
      expression =
          "java(order.getItems().stream()"
          + ".map(i -> new OrderDto.OrderItemDto("
          + "  i.getId(),"
          + "  i.getProduct().getId(),"
          + "  i.getQuantity(),"
          + "  i.getUnitPrice(),"
          + "  i.getLineTotal()"
          + "))"
          + ".collect(Collectors.toList()))"
  )
  OrderDto toDto(Order order);
}
