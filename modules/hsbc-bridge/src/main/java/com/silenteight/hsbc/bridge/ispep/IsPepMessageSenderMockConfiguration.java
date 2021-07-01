package com.silenteight.hsbc.bridge.ispep;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
@Slf4j
class IsPepMessageSenderMockConfiguration {

  @Bean
  IsPepMessageSender warehouseIsPepMessageSender() {
    return isPepLearningStoreExchangeRequest ->
        log.warn("IsPepLearningStoreExchangeRequest has been sent");
  }
}
