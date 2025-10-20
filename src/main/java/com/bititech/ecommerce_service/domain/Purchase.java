package com.bititech.ecommerce_service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name="purchases")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Purchase extends BaseEntity {
  @Column(nullable=false)
  private String supplierName;
  private String referenceNo;

  @Column(nullable=false, precision=12, scale=2)
  private BigDecimal totalAmount;

  private String note;

  @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<PurchaseItem> items = new ArrayList<>();
}
