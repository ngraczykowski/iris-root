package com.silenteight.hsbc.bridge.report;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Slf4j
class WarehouseSenderConfiguration {

  @Bean
  @Profile("dev")
  public AlertSender alertSenderMock() {
    return alerts -> log.warn("Alerts have been sent, size={}", alerts.size());
  }

  @Bean
  @Profile("dev")
  public AlertWithRecommendationSender alertWithRecommendationSenderMock() {
    return alerts -> log.warn("Alerts with recommendations have been sent, size={}", alerts.size());
  }
}
