package com.bititech.ecommerce_service.config;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * Supplies the current auditor (username/email) to Spring Data auditing.
 * Works with our JwtAuthFilter which sets Authentication with the user's email as principal.
 */
public class AuditorAwareImpl implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
      return Optional.of("system");
    }
    // auth.getName() resolves to the subject/email we set in JwtAuthFilter
    String username = auth.getName();
    if (username == null || username.isBlank() || "anonymousUser".equals(username)) {
      return Optional.of("system");
    }
    return Optional.of(username);
  }
}
