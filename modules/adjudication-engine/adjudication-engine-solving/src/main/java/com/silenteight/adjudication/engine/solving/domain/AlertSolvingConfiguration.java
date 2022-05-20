package com.silenteight.adjudication.engine.solving.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AlertSolvingConfiguration {

  @Bean
  AlertSolvingFactory alertSolvingFactory(
      final AlertSolvingRepository alertSolvingRepository
  ) {
    return new AlertSolvingFactory(alertSolvingRepository);
  }
}
