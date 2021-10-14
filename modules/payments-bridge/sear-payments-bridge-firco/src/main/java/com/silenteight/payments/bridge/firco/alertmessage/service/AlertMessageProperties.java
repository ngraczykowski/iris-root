package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties("pb.alert-message")
@Data
class AlertMessageProperties {

  private long storedQueueLimit = 1000;

  private Duration decisionRequestedTime = Duration.ofSeconds(60);

  private boolean originalMessageDeletedAfterRecommendation = true;

  private int maxHitsPerAlert = 10;

}
