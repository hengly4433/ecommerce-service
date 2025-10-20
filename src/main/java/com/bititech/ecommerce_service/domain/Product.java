package com.bititech.ecommerce_service.domain;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.List;

@Entity @Table(name="products")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Product extends BaseEntity {
  @Column(nullable=false, unique=true, length=120)
  private String sku;

  @Column(nullable=false)
  private String name;

  private String description;

  @Column(nullable=false, precision=12, scale=2)
  private BigDecimal price;

  private String mainImageUrl;

  @Type(JsonType.class)
  @Column(columnDefinition = "jsonb not null default '[]'")
  private List<String> imageUrls;

  @ManyToOne @JoinColumn(name="category_id")
  private Category category;

  @ManyToOne @JoinColumn(name="store_id")
  private Store store;
}
