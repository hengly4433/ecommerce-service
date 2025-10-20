package com.bititech.ecommerce_service.mapper;

import org.mapstruct.Mapper;

import com.bititech.ecommerce_service.domain.Permission;
import com.bititech.ecommerce_service.dto.permission.PermissionDto;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
  PermissionDto toDto(Permission entity);
}
