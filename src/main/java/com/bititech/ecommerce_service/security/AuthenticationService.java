package com.bititech.ecommerce_service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bititech.ecommerce_service.domain.Role;
import com.bititech.ecommerce_service.domain.User;
import com.bititech.ecommerce_service.exception.BusinessException;
import com.bititech.ecommerce_service.repository.RoleRepository;
import com.bititech.ecommerce_service.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository users;
  private final RoleRepository roles;
  private final BCryptPasswordEncoder encoder;
  private final JwtService jwt;

  public String issueToken(User u) {
    Set<String> auth = u.getRoles().stream()
        .flatMap(r -> r.getPermissions().stream())
        .map(p -> "PERM_" + p.getCode())
        .collect(Collectors.toSet());
    return jwt.generate(u.getEmail(), auth);
  }

  public User registerAdminIfMissing() {
    return users.findByEmail("admin@local").orElseGet(() -> {
      Role admin = roles.findByCode("ADMIN").orElseThrow();
      User u = User.builder()
          .email("admin@local")
          .passwordHash(encoder.encode("password"))
          .fullName("System Admin")
          .enabled(true)
          .locked(false)
          .emailVerified(true)
          .build();
      u.getRoles().add(admin);
      return users.save(u);
    });
  }

  public User register(String email, String rawPassword, String fullName, Set<String> roleCodes) {
    Optional<User> existing = users.findByEmail(email);
    if (existing.isPresent()) {
      throw new BusinessException("Email already registered");
    }

    User u = User.builder()
        .email(email)
        .passwordHash(encoder.encode(rawPassword))
        .fullName(fullName)
        .enabled(true)
        .locked(false)
        .emailVerified(false) // must verify via OTP
        .build();

    Set<Role> assigned = new HashSet<>();
    if (roleCodes != null && !roleCodes.isEmpty()) {
      for (String code : roleCodes) {
        Role r = roles.findByCode(code)
            .orElseThrow(() -> new BusinessException("Unknown role: " + code));
        assigned.add(r);
      }
    } else {
      roles.findByCode("CUSTOMER").ifPresent(assigned::add);
    }
    u.setRoles(assigned);

    return users.save(u);
  }
}
