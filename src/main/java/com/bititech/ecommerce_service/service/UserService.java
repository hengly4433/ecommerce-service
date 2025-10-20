package com.bititech.ecommerce_service.service;

import org.springframework.data.domain.*;

import com.bititech.ecommerce_service.dto.user.UserCreateDto;
import com.bititech.ecommerce_service.dto.user.UserDto;

public interface UserService {
  Page<UserDto> list(Pageable pageable);
  UserDto get(Long id);
  UserDto create(UserCreateDto dto);
  UserDto enable(Long id, boolean enabled);
  UserDto lock(Long id, boolean locked);
  void delete(Long id);
}
