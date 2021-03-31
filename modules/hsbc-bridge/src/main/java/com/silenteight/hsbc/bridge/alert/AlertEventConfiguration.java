package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class AlertEventConfiguration {

  private final AlertRepository alertRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Bean
  AlertEventHandler alertEventHandler() {
    return new AlertEventHandler(alertRepository, eventPublisher);
  }
}
