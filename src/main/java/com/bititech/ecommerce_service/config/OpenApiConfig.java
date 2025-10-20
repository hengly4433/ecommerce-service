package com.bititech.ecommerce_service.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI ecommerceOpenAPI() {
    return new OpenAPI().info(new Info()
        .title("E-Commerce Service API")
        .version("v1")
        .description("OpenAPI spec for the E-Commerce service"));
  }
}