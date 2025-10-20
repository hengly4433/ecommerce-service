package com.bititech.ecommerce_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bititech.ecommerce_service.domain.Address;
import com.bititech.ecommerce_service.domain.Store;
import com.bititech.ecommerce_service.dto.store.StoreDto;
import com.bititech.ecommerce_service.exception.NotFoundException;
import com.bititech.ecommerce_service.mapper.StoreMapper;
import com.bititech.ecommerce_service.repository.StoreRepository;
import com.bititech.ecommerce_service.service.StoreService;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreServiceImpl implements StoreService {

  private final StoreRepository stores;
  private final StoreMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public Page<StoreDto> list(Pageable pageable) {
    return stores.findAll(pageable).map(mapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public StoreDto get(Long id) {
    Store s = stores.findById(id).orElseThrow(() -> new NotFoundException("Store not found"));
    return mapper.toDto(s);
  }

  @Override
  public StoreDto create(String code, String name, String addressLine, String city, String country) {
    Address addr = new Address();
    addr.setAddressLine(addressLine);
    addr.setCity(city);
    addr.setCountry(country);

    Store s = Store.builder()
        .code(code)
        .name(name)
        .address(addr)
        .build();
    return mapper.toDto(stores.save(s));
  }

  @Override
  public StoreDto rename(Long id, String name) {
    Store s = stores.findById(id).orElseThrow(() -> new NotFoundException("Store not found"));
    s.setName(name);
    return mapper.toDto(s);
  }

  @Override
  public void delete(Long id) {
    stores.deleteById(id);
  }
}
