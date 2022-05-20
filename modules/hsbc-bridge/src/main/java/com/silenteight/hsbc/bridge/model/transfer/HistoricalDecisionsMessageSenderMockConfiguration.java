package com.silenteight.hsbc.bridge.model.transfer;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Profile("dev")
@Configuration
class HistoricalDecisionsMessageSenderMockConfiguration {

  @Bean
  HistoricalDecisionsMessageSender historicalDecisionsMessageSender() {
    return modelPersisted -> log.warn("ModelPersisted has been sent to HistoricalDecisions");
  }
}
