package com.bititech.ecommerce_service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity @Table(name="purchase_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PurchaseItem extends BaseEntity {
  @ManyToOne(optional=false) @JoinColumn(name="purchase_id")
  private Purchase purchase;

  @ManyToOne(optional=false) @JoinColumn(name="product_id")
  private Product product;

  @Column(nullable=false, precision=12, scale=2)
  private BigDecimal unitCost;

  @Column(nullable=false)
  private long quantity;

  @Column(nullable=false, precision=12, scale=2)
  private BigDecimal lineTotal;
}
