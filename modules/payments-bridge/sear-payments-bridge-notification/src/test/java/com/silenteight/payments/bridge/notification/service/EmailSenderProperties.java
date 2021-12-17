package com.silenteight.payments.bridge.notification.service;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "spring.mail")
@Validated
@Data
class EmailSenderProperties {

  private String host;

  private Integer port;

  private String username;

  private String password;
}
