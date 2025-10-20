package com.bititech.ecommerce_service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.bititech.ecommerce_service.repository.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DatabaseUserDetailsService implements UserDetailsService {

  private final UserRepository users;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user = users.findWithRolesByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    // Aggregate authorities PERM_<code>
    Set<SimpleGrantedAuthority> auth = user.getRoles().stream()
        .flatMap(r -> r.getPermissions().stream())
        .map(p -> new SimpleGrantedAuthority("PERM_" + p.getCode()))
        .collect(Collectors.toSet());

    return org.springframework.security.core.userdetails.User
        .withUsername(user.getEmail())
        .password(user.getPasswordHash())
        .authorities(auth)
        .accountLocked(user.isLocked())
        .disabled(!user.isEnabled())
        .build();
  }
}
