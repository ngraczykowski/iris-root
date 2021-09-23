package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties("pb.alert-message")
@Data
class AlertMessageProperties {

  private long storedQueueLimit = 1000;

  private Duration decisionRequestedTime = Duration.ofSeconds(30);

}
