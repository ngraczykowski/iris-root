package com.silenteight.connector.ftcc.callback.response;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "ftcc.cmapi.callback")
public class RecommendationSenderProperties {

  private String endpoint;
  private boolean enabled = true;
  private String login;
  private String password;
  private Duration readTimeout = Duration.ofSeconds(10);
  private Duration connectionTimeout = Duration.ofSeconds(10);
}
