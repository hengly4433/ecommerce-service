package com.bititech.ecommerce_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bititech.ecommerce_service.domain.Role;
import com.bititech.ecommerce_service.domain.User;
import com.bititech.ecommerce_service.dto.user.UserCreateDto;
import com.bititech.ecommerce_service.dto.user.UserDto;
import com.bititech.ecommerce_service.exception.NotFoundException;
import com.bititech.ecommerce_service.mapper.UserMapper;
import com.bititech.ecommerce_service.repository.RoleRepository;
import com.bititech.ecommerce_service.repository.UserRepository;
import com.bititech.ecommerce_service.service.UserService;

@Service @RequiredArgsConstructor @Transactional
public class UserServiceImpl implements UserService {
  private final UserRepository users;
  private final RoleRepository roles;
  private final UserMapper mapper;
  private final BCryptPasswordEncoder encoder;

  @Override
  @Transactional(readOnly = true)
  public Page<UserDto> list(Pageable pageable) {
    return users.findAll(pageable).map(mapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public UserDto get(Long id) {
    var u = users.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    return mapper.toDto(u);
  }

  @Override
  public UserDto create(UserCreateDto dto) {
    User u = User.builder()
        .email(dto.email())
        .passwordHash(encoder.encode(dto.password()))
        .fullName(dto.fullName())
        .enabled(true)
        .locked(false)
        .build();
    if (dto.roleCodes() != null) {
      for (var code : dto.roleCodes()) {
        Role r = roles.findByCode(code).orElseThrow(() -> new NotFoundException("Role not found: " + code));
        u.getRoles().add(r);
      }
    }
    return mapper.toDto(users.save(u));
  }

  @Override
  public UserDto enable(Long id, boolean enabled) {
    var u = users.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    u.setEnabled(enabled);
    return mapper.toDto(u);
  }

  @Override
  public UserDto lock(Long id, boolean locked) {
    var u = users.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    u.setLocked(locked);
    return mapper.toDto(u);
  }

  @Override
  public void delete(Long id) {
    users.deleteById(id);
  }
}
