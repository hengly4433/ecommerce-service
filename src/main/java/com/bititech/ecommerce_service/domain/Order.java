package com.bititech.ecommerce_service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name="orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Order extends BaseEntity {
  @Column(nullable=false, unique=true)
  private String orderNo;

  @ManyToOne @JoinColumn(name="customer_id")
  private Customer customer;

  @ManyToOne @JoinColumn(name="store_id")
  private Store store;

  @Enumerated(EnumType.STRING)
  @Column(nullable=false)
  private Status status;

  public enum Status { NEW, PAID, SHIPPED, COMPLETED, CANCELLED }

  @Column(nullable=false, precision=12, scale=2)
  private BigDecimal subtotal;

  @Column(nullable=false, precision=12, scale=2)
  private BigDecimal discount;

  @Column(nullable=false, precision=12, scale=2)
  private BigDecimal tax;

  @Column(nullable=false, precision=12, scale=2)
  private BigDecimal total;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<OrderItem> items = new ArrayList<>();
}
