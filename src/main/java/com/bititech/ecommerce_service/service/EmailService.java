package com.bititech.ecommerce_service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

  @Autowired(required = false) @Nullable
  private JavaMailSender mailSender;

  @Value("${app.auth.email.from:no-reply@local}")
  private String from;

  public void send(String to, String subject, String body) {
    if (mailSender == null) {
      log.info("[EMAIL-FAKE] To: {} | Subject: {} | Body: {}", to, subject, body);
      return;
    }
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setFrom(from);
    msg.setTo(to);
    msg.setSubject(subject);
    msg.setText(body);
    mailSender.send(msg);
  }
}
