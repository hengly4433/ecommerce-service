package com.bititech.ecommerce_service.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.bititech.ecommerce_service.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

  @Query("select u from User u left join fetch u.roles r left join fetch r.permissions where u.email=:email")
  Optional<User> findWithRolesByEmail(@Param("email") String email);
}
