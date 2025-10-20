package com.bititech.ecommerce_service.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="customers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Customer extends BaseEntity {
  @Column(nullable=false, unique=true, length=320)
  private String email;
  @Column(nullable=false)
  private String fullName;
  private String phone;
  private String shippingAddress;
}
