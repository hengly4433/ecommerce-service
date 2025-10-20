package com.bititech.ecommerce_service.service;

import org.springframework.data.domain.*;

import com.bititech.ecommerce_service.dto.order.OrderCreateDto;
import com.bititech.ecommerce_service.dto.order.OrderDto;

public interface OrderService {
  Page<OrderDto> list(Pageable pageable);
  OrderDto get(Long id);
  OrderDto create(OrderCreateDto dto);
  OrderDto markPaid(Long id);
  OrderDto updateStatus(Long id, String status);
  void delete(Long id);
}
