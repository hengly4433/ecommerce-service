package com.bititech.ecommerce_service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity @Table(name="roles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Role extends BaseEntity {
  @Column(nullable=false, unique=true, length=100)
  private String code;

  @Column(nullable=false)
  private String name;

  @ManyToMany
  @JoinTable(name="role_permissions",
      joinColumns=@JoinColumn(name="role_id"),
      inverseJoinColumns=@JoinColumn(name="permission_id"))
  @Builder.Default
  private Set<Permission> permissions = new HashSet<>();
}