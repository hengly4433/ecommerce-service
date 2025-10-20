package com.bititech.ecommerce_service.security;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.bititech.ecommerce_service.domain.User;
import com.bititech.ecommerce_service.dto.auth.ConfirmEmailRequest;
import com.bititech.ecommerce_service.dto.auth.LoginRequest;
import com.bititech.ecommerce_service.dto.auth.LoginResponse;
import com.bititech.ecommerce_service.dto.auth.PasswordResetConfirm;
import com.bititech.ecommerce_service.dto.auth.PasswordResetRequest;
import com.bititech.ecommerce_service.dto.auth.RegisterRequest;
import com.bititech.ecommerce_service.dto.auth.VerifyEmailRequest;
import com.bititech.ecommerce_service.exception.BusinessException;
import com.bititech.ecommerce_service.repository.UserRepository;
import com.bititech.ecommerce_service.service.EmailService;
import com.bititech.ecommerce_service.service.OtpService;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserRepository users;
  private final BCryptPasswordEncoder encoder;
  private final AuthenticationService auth;
  private final OtpService otp;
  private final EmailService email;
  private final JwtService jwt;
  private final RevokedTokenService revoked;

  /** POST /api/auth/login — Username/password → JWT */
  @PostMapping("/login")
  public LoginResponse login(@RequestBody @Valid LoginRequest req) {
    var u = users.findWithRolesByEmail(req.email())
        .orElseThrow(() -> new BusinessException("Invalid credentials"));
    if (!u.isEnabled() || u.isLocked() || !encoder.matches(req.password(), u.getPasswordHash())) {
      throw new BusinessException("Invalid credentials");
    }
    String token = auth.issueToken(u);
    return new LoginResponse(token, 120 * 60);
  }

  /** POST /api/auth/register — public signup → returns JWT, kicks off email verification OTP */
  @PostMapping("/register")
  public LoginResponse register(@RequestBody @Valid RegisterRequest req) {
    User u = auth.register(req.email(), req.password(), req.fullName(), req.roleCodes());
    String code = otp.createVerifyCode(u.getEmail());
    email.send(u.getEmail(), "Verify your email", "Your verification code is: " + code + " (valid 10 minutes)");
    String token = auth.issueToken(u);
    return new LoginResponse(token, 120 * 60);
  }

  /** POST /api/auth/logout — revoke current access token (deny-list) */
  @PostMapping("/logout")
  public void logout(Authentication principal) {
    if (principal == null) return;
    String token = (String) principal.getCredentials();
    String jti = jwt.getJti(token);
    long ttl = jwt.secondsUntilExpiry(token);
    revoked.revoke(jti, ttl);
  }

  /** POST /api/auth/verify-email/request — resend verification OTP */
  @PostMapping("/verify-email/request")
  public void requestVerify(@RequestBody @Valid VerifyEmailRequest req) {
    Optional<User> u = users.findByEmail(req.email());
    if (u.isEmpty()) {
      // Do not reveal user existence to callers; silently succeed
      return;
    }
    if (u.get().isEmailVerified()) return;
    String code = otp.createVerifyCode(req.email());
    email.send(req.email(), "Verify your email", "Your verification code is: " + code + " (valid 10 minutes)");
  }

  /** POST /api/auth/verify-email/confirm — confirm OTP and mark emailVerified = true */
  @PostMapping("/verify-email/confirm")
  public void confirmVerify(@RequestBody @Valid ConfirmEmailRequest req) {
    var u = users.findByEmail(req.email()).orElseThrow(() -> new BusinessException("Account not found"));
    if (u.isEmailVerified()) return;
    if (!otp.validateVerifyCode(req.email(), req.code())) {
      throw new BusinessException("Invalid or expired verification code");
    }
    u.setEmailVerified(true);
    users.save(u);
  }

  /** POST /api/auth/password-reset/request — send reset OTP */
  @PostMapping("/password-reset/request")
  public void resetRequest(@RequestBody @Valid PasswordResetRequest req) {
    Optional<User> u = users.findByEmail(req.email());
    if (u.isEmpty()) return; // don't reveal
    String code = otp.createResetCode(req.email());
    email.send(req.email(), "Reset your password", "Your password reset code is: " + code + " (valid 10 minutes)");
  }

  /** POST /api/auth/password-reset/confirm — confirm OTP and set new password */
  @PostMapping("/password-reset/confirm")
  public void resetConfirm(@RequestBody @Valid PasswordResetConfirm req) {
    var u = users.findByEmail(req.email()).orElseThrow(() -> new BusinessException("Account not found"));
    if (!otp.validateResetCode(req.email(), req.code())) {
      throw new BusinessException("Invalid or expired reset code");
    }
    u.setPasswordHash(encoder.encode(req.newPassword()));
    users.save(u);
  }
}
