package com.bititech.ecommerce_service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RevokedTokenService {

  private final StringRedisTemplate redis;

  private String key(String jti) { return "jwt:revoked:" + jti; }

  public void revoke(String jti, long ttlSeconds) {
    if (jti == null || jti.isBlank()) return;
    redis.opsForValue().set(key(jti), "1", Duration.ofSeconds(Math.max(10, ttlSeconds)));
  }

  public boolean isRevoked(String jti) {
    if (jti == null || jti.isBlank()) return false;
    Boolean exists = redis.hasKey(key(jti));
    return exists != null && exists;
  }
}
