package com.bititech.ecommerce_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Service
public class JwtService {

  @Value("${app.security.jwt.issuer}")
  private String issuer;

  @Value("${app.security.jwt.base64-secret}")
  private String base64Secret;

  @Value("${app.security.jwt.ttl-minutes}")
  private long ttlMinutes;

  /** HS256 key (must be ≥ 256 bits). */
  private SecretKey key;

  @PostConstruct
  void init() {
    byte[] secret = Base64.getDecoder().decode(base64Secret);
    this.key = Keys.hmacShaKeyFor(secret); // returns SecretKey
  }

  /** Generate a signed JWS. JJWT picks HS algorithm from the SecretKey size. */
  public String generate(String subject, Set<String> authorities) {
    Instant now = Instant.now();
    Instant exp = now.plusSeconds(ttlMinutes * 60);
    String jti = UUID.randomUUID().toString();
    return Jwts.builder()
        .issuer(issuer)
        .subject(subject)
        .id(jti)
        .issuedAt(Date.from(now))
        .expiration(Date.from(exp))
        .claim("scope", String.join(" ", authorities))
        .signWith(key)               // ✅ 0.12.x style (no deprecated overloads)
        .compact();
  }

  /** Parse and verify a signed JWS (HMAC). */
  public Jws<Claims> parse(String token) {
    return Jwts.parser()
        .verifyWith(key)             // ✅ expects SecretKey for HMAC
        .build()
        .parseSignedClaims(token);
  }

  public String getJti(String token) {
    try { return parse(token).getPayload().getId(); }
    catch (Exception e) { return null; }
  }

  public long secondsUntilExpiry(String token) {
    try {
      Date exp = parse(token).getPayload().getExpiration();
      return Math.max(0, (exp.getTime() - System.currentTimeMillis()) / 1000);
    } catch (Exception e) {
      return 0;
    }
  }
}
