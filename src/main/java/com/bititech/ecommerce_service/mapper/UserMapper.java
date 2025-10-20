package com.bititech.ecommerce_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.bititech.ecommerce_service.domain.User;
import com.bititech.ecommerce_service.dto.user.UserDto;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {Collectors.class})
public interface UserMapper {

  @Mapping(
      target = "roles",
      expression = "java(user.getRoles().stream().map(r -> r.getCode()).collect(Collectors.toSet()))"
  )
  UserDto toDto(User user);
}
