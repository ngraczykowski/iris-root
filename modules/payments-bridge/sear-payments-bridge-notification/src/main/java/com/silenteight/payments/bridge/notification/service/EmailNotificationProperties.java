package com.silenteight.payments.bridge.notification.service;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;


@ConfigurationProperties(prefix = "pb.email-notification")
@Validated
@Data
public class EmailNotificationProperties {

  private static final String DEFAULT_CRON = "0 0/10 * * * *";
  private static final int DEFAULT_AMOUNT = 10;

  private String from;
  private String to;
  private String cron = DEFAULT_CRON;
  private int amount = DEFAULT_AMOUNT;
}
