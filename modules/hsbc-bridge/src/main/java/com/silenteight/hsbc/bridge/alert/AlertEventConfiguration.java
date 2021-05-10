package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class AlertEventConfiguration {

  private final AlertRepository alertRepository;

  @Bean
  AlertEventListener alertEventHandler() {
    return new AlertEventListener(alertUpdater());
  }

  private AlertUpdater alertUpdater() {
    return new AlertUpdater(alertRepository);
  }
}
