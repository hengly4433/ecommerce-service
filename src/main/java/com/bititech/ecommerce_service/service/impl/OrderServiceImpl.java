package com.bititech.ecommerce_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bititech.ecommerce_service.domain.Customer;
import com.bititech.ecommerce_service.domain.Order;
import com.bititech.ecommerce_service.domain.OrderItem;
import com.bititech.ecommerce_service.domain.Store;
import com.bititech.ecommerce_service.dto.order.OrderCreateDto;
import com.bititech.ecommerce_service.dto.order.OrderDto;
import com.bititech.ecommerce_service.exception.BusinessException;
import com.bititech.ecommerce_service.exception.NotFoundException;
import com.bititech.ecommerce_service.mapper.OrderMapper;
import com.bititech.ecommerce_service.repository.CustomerRepository;
import com.bititech.ecommerce_service.repository.InventoryRepository;
import com.bititech.ecommerce_service.repository.OrderItemRepository;
import com.bititech.ecommerce_service.repository.OrderRepository;
import com.bititech.ecommerce_service.repository.ProductRepository;
import com.bititech.ecommerce_service.repository.StoreRepository;
import com.bititech.ecommerce_service.service.OrderService;

import java.math.BigDecimal;
import java.util.UUID;

@Service @RequiredArgsConstructor @Transactional
public class OrderServiceImpl implements OrderService {
  private final OrderRepository orders;
  private final OrderItemRepository orderItems;
  private final CustomerRepository customers;
  private final StoreRepository stores;
  private final ProductRepository products;
  private final InventoryRepository inventoryRepo;
  private final OrderMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public Page<OrderDto> list(Pageable pageable) {
    return orders.findAll(pageable).map(mapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public OrderDto get(Long id) {
    var o = orders.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
    return mapper.toDto(o);
  }

  @Override
  public OrderDto create(OrderCreateDto dto) {
    Customer c = customers.findById(dto.customerId()).orElseThrow(() -> new NotFoundException("Customer not found"));
    Store s = dto.storeId() == null ? null : stores.findById(dto.storeId()).orElse(null);

    BigDecimal subtotal = BigDecimal.ZERO;
    Order o = Order.builder()
        .orderNo("ORD-" + UUID.randomUUID().toString().substring(0,8).toUpperCase())
        .customer(c)
        .store(s)
        .status(Order.Status.NEW)
        .discount(dto.discount())
        .tax(dto.tax())
        .subtotal(BigDecimal.ZERO)
        .total(BigDecimal.ZERO)
        .build();
    o = orders.save(o);

    for (var it : dto.items()) {
      var p = products.findById(it.productId()).orElseThrow(() -> new NotFoundException("Product not found: "+it.productId()));
      var line = it.unitPrice().multiply(BigDecimal.valueOf(it.quantity()));
      subtotal = subtotal.add(line);
      var oi = OrderItem.builder()
          .order(o).product(p)
          .unitPrice(it.unitPrice())
          .quantity(it.quantity())
          .lineTotal(line)
          .build();
      orderItems.save(oi);
      o.getItems().add(oi);
    }
    o.setSubtotal(subtotal);
    o.setTotal(subtotal.subtract(o.getDiscount()).add(o.getTax()));
    return mapper.toDto(o);
  }

  @Override
  public OrderDto markPaid(Long id) {
    var o = orders.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
    if (o.getStatus() != Order.Status.NEW) throw new BusinessException("Only NEW orders can be marked PAID");
    // deduct inventory
    for (var it : o.getItems()) {
      var inv = inventoryRepo.findByProduct(it.getProduct())
          .orElseThrow(() -> new BusinessException("Inventory missing for product "+it.getProduct().getId()));
      if (inv.getQuantity() < it.getQuantity()) throw new BusinessException("Out of stock for product "+it.getProduct().getId());
      inv.setQuantity(inv.getQuantity() - it.getQuantity());
    }
    o.setStatus(Order.Status.PAID);
    return mapper.toDto(o);
  }

  @Override
  public OrderDto updateStatus(Long id, String status) {
    var o = orders.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
    o.setStatus(Order.Status.valueOf(status));
    return mapper.toDto(o);
  }

  @Override
  public void delete(Long id) {
    orders.deleteById(id);
  }
}
