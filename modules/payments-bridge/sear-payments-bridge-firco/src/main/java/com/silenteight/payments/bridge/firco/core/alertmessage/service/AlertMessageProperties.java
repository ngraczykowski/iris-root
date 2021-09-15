package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties("pb.firco.alert-message")
@Data
class AlertMessageProperties {

  private long queueMessageStoredLimit = 1000;

  private Duration issueDecisionRequestedTime = Duration.ofSeconds(60);

}
