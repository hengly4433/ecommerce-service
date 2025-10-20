package com.bititech.ecommerce_service.audit;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@MappedSuperclass
@Getter
public abstract class Auditable {
  @Column(name = "created_at", nullable = false, updatable = false)
  protected Instant createdAt = Instant.now();

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  protected Instant updatedAt = Instant.now();

  @Column(name = "created_by")
  protected String createdBy;

  @Column(name = "updated_by")
  protected String updatedBy;

  public void setCreatedBy(String v) { this.createdBy = v; }
  public void setUpdatedBy(String v) { this.updatedBy = v; }
}