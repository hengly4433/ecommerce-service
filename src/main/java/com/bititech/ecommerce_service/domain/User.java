package com.bititech.ecommerce_service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity @Table(name="users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User extends BaseEntity {
  @Column(nullable=false, unique=true, length=320)
  private String email;

  @Column(name="password_hash", nullable=false)
  private String passwordHash;

  @Column(nullable=false)
  private String fullName;

  @Column(nullable=false)
  private boolean enabled;

  @Column(nullable=false)
  private boolean locked;

  @Column(name="email_verified", nullable=false)
  private boolean emailVerified;

  private String profileImageUrl;

  @ManyToMany
  @JoinTable(name="user_roles",
      joinColumns=@JoinColumn(name="user_id"),
      inverseJoinColumns=@JoinColumn(name="role_id"))
  @Builder.Default
  private Set<Role> roles = new HashSet<>();
}
