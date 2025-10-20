package com.bititech.ecommerce_service.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="permissions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Permission extends BaseEntity {
  @Column(nullable=false, unique=true, length=100)
  private String code;

  @Column(nullable=false)
  private String name;
}