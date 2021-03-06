package com.silenteight.payments.bridge.notification.service;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;


@ConfigurationProperties(prefix = "pb.email-notification")
@Validated
@Data
public class EmailNotificationProperties {

  private static final String DEFAULT_CRON = "0 0/10 * * * *";
  private static final int DEFAULT_AMOUNT = 10;
  private static final String DEFAULT_FROM = "from@s8.com";
  private static final String DEFAULT_TO = "to@s8.com";
  private static final String DEFAULT_CC = "cc@s8.com";

  private String from = DEFAULT_FROM;
  private String to = DEFAULT_TO;
  private String cc = DEFAULT_CC;
  private String cron = DEFAULT_CRON;
  private int amount = DEFAULT_AMOUNT;
  private boolean cmapiEnabled = false;
  private List<String> cmapiErrorsEnabled = new ArrayList<>();
  private boolean learningEnabled = false;
}
