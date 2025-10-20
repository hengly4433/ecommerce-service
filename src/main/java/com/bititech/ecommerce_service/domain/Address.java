package com.bititech.ecommerce_service.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Address {
  private String addressLine;
  private String city;
  private String country;
}
