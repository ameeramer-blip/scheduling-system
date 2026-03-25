package com.memcyco.scheduler.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

  @Value("${app.cors.allowed-origins:http://localhost:5173}")
  private String allowedOriginsRaw;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    List<String> allowedOrigins = parseOrigins(allowedOriginsRaw);
    registry.addMapping("/api/**")
        .allowedOrigins(allowedOrigins.toArray(new String[0]))
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*");
  }

  private static List<String> parseOrigins(String raw) {
    if (raw == null || raw.isBlank()) {
      return List.of("http://localhost:5173");
    }
    // supports either a single origin or comma-separated origins
    return Arrays.stream(raw.split(","))
        .map(String::trim)
        .filter(s -> !s.isBlank())
        .distinct()
        .toList();
  }
}

