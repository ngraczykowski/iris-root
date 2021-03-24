package com.silenteight.hsbc.bridge.alert;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AlertEventConfiguration {

  @Bean
  AlertEventHandler alertEventHandler(AlertRepository alertRepository) {
    return new AlertEventHandler(alertRepository);
  }
}
