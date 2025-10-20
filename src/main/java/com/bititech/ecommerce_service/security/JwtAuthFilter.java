package com.bititech.ecommerce_service.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwt;
  private final RevokedTokenService revoked;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (auth != null && auth.startsWith("Bearer ")) {
      String token = auth.substring(7);
      try {
        String jti = jwt.getJti(token);
        if (revoked.isRevoked(jti)) {
          response.setStatus(401);
          response.setContentType("application/json");
          response.getWriter().write("""
            {"status":401,"error":"Unauthorized","message":"Token revoked"}
          """);
          return;
        }
        var jws = jwt.parse(token);
        Claims c = jws.getPayload();
        String sub = c.getSubject();
        String scope = c.get("scope", String.class);
        Collection<GrantedAuthority> authorities = scope == null ? List.of()
            : Arrays.stream(scope.split(" "))
                .filter(s -> !s.isBlank())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        Authentication a = new UsernamePasswordAuthenticationToken(sub, token, authorities);
        SecurityContextHolder.getContext().setAuthentication(a);
      } catch (Exception e) {
        // invalid token -> leave unauthenticated
      }
    }
    chain.doFilter(request, response);
  }
}
