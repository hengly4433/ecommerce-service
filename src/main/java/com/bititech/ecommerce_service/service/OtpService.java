package com.bititech.ecommerce_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class OtpService {

  private final StringRedisTemplate redis;
  private static final SecureRandom RNG = new SecureRandom();

  @Value("${app.auth.otp.ttl-minutes:10}")
  private int otpTtlMinutes;

  private String key(String prefix, String email) { return "otp:" + prefix + ":" + email.toLowerCase(); }

  private String genCode() {
    int code = 100000 + RNG.nextInt(900000);
    return Integer.toString(code);
  }

  public String createVerifyCode(String email) {
    String code = genCode();
    redis.opsForValue().set(key("verify", email), code, Duration.ofMinutes(otpTtlMinutes));
    return code;
  }

  public boolean validateVerifyCode(String email, String code) {
    String k = key("verify", email);
    String val = redis.opsForValue().get(k);
    if (val != null && val.equals(code)) {
      redis.delete(k);
      return true;
    }
    return false;
  }

  public String createResetCode(String email) {
    String code = genCode();
    redis.opsForValue().set(key("reset", email), code, Duration.ofMinutes(otpTtlMinutes));
    return code;
  }

  public boolean validateResetCode(String email, String code) {
    String k = key("reset", email);
    String val = redis.opsForValue().get(k);
    if (val != null && val.equals(code)) {
      redis.delete(k);
      return true;
    }
    return false;
  }
}
