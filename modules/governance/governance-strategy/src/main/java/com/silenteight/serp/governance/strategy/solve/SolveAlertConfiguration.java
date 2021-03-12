package com.silenteight.serp.governance.strategy.solve;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SolveAlertConfiguration {

  @Bean
  SolveAlertUseCase solveAlertUseCase() {
    return new SolveAlertUseCase();
  }
}
