package com.bititech.ecommerce_service.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="inventory")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Inventory extends BaseEntity {
  @OneToOne(optional=false) @JoinColumn(name="product_id", unique=true)
  private Product product;

  @Column(nullable=false)
  private long quantity;

  @Column(nullable=false)
  private long minimumThreshold;
}
