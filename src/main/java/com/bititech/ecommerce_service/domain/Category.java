package com.bititech.ecommerce_service.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="categories")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Category extends BaseEntity {
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="parent_id")
  private Category parent;

  @Column(nullable=false)
  private String name;

  @Column(nullable=false, unique=true)
  private String slug;
}
