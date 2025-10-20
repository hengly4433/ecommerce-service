package com.bititech.ecommerce_service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity @Table(name="order_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItem extends BaseEntity {
  @ManyToOne(optional=false) @JoinColumn(name="order_id")
  private Order order;

  @ManyToOne(optional=false) @JoinColumn(name="product_id")
  private Product product;

  @Column(nullable=false, precision=12, scale=2)
  private BigDecimal unitPrice;

  @Column(nullable=false)
  private long quantity;

  @Column(nullable=false, precision=12, scale=2)
  private BigDecimal lineTotal;
}
