package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class AlertFacadeConfiguration {

  private final AlertRepository alertRepository;

  @Bean
  AlertFacade alertFacade() {
    return AlertFacade.builder()
        .alertRawMapper(new AlertRawMapper())
        .alertRepository(alertRepository)
        .build();
  }
}
