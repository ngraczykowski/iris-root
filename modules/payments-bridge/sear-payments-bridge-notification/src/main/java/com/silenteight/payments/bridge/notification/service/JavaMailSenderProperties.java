package com.silenteight.payments.bridge.notification.service;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "spring.mail")
@Validated
@Data
class JavaMailSenderProperties {

  private static final String DEFAULT_HOST = "localhost";
  private static final int DEFAULT_PORT = 24170;
  private static final String DEFAULT_USERNAME = "user";
  private static final String DEFAULT_PASSWORD = "password";

  private String host = DEFAULT_HOST;
  private Integer port = DEFAULT_PORT;
  private String username = DEFAULT_USERNAME;
  private String password = DEFAULT_PASSWORD;
}
