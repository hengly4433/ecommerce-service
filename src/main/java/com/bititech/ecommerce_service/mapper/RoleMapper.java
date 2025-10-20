package com.bititech.ecommerce_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.bititech.ecommerce_service.domain.Role;
import com.bititech.ecommerce_service.dto.role.RoleDto;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {Collectors.class})
public interface RoleMapper {

  @Mapping(
      target = "permissions",
      expression = "java(role.getPermissions().stream().map(p -> p.getCode()).collect(Collectors.toSet()))"
  )
  RoleDto toDto(Role role);
}
