package com.bititech.ecommerce_service.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@Order(0) // run very early
public class RateLimitFilter implements Filter {

  @Value("${app.security.ratelimit.auth.capacity:10}")
  private int capacity;

  @Value("${app.security.ratelimit.auth.refill-seconds:60}")
  private int refillSeconds;

  private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

  private Bucket resolveBucket(String key) {
    return buckets.computeIfAbsent(key, k -> Bucket.builder()
        .addLimit(Bandwidth.classic(capacity, Refill.intervally(capacity, Duration.ofSeconds(refillSeconds))))
        .build());
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    String path = req.getRequestURI();
    if (!path.startsWith("/api/auth/")) {
      chain.doFilter(request, response);
      return;
    }
    String ip = req.getRemoteAddr();
    Bucket bucket = resolveBucket("auth:" + ip);
    if (bucket.tryConsume(1)) {
      chain.doFilter(request, response);
    } else {
      HttpServletResponse resp = (HttpServletResponse) response;
      resp.setStatus(429);
      resp.setContentType("application/json");
      resp.getWriter().write("""
        {"status":429,"error":"Too Many Requests","message":"Rate limit exceeded on /api/auth/*"}
      """);
    }
  }
}
