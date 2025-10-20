package com.bititech.ecommerce_service.mapper;

import org.mapstruct.Mapper;

import com.bititech.ecommerce_service.domain.Customer;
import com.bititech.ecommerce_service.dto.customer.CustomerDto;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
  CustomerDto toDto(Customer entity);
}
