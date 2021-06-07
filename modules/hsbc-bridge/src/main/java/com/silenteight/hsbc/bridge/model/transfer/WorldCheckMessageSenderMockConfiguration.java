package com.silenteight.hsbc.bridge.model.transfer;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
@Slf4j
class WorldCheckMessageSenderMockConfiguration {

  @Bean
  WorldCheckMessageSender worldCheckMessageSenderMock() {
    return modelPersisted -> log.warn("ModelPersisted has been sent");
  }
}
