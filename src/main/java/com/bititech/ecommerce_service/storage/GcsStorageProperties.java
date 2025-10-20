package com.bititech.ecommerce_service.storage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix="app.storage.gcs")
public class GcsStorageProperties {
  private String bucket;
  private String publicUrlPrefix;
  private String credentialsJson; // optional dev only
}
