package com.silenteight.serp.governance.strategy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.serp.governance.strategy.SolvingStrategyType.USE_ANALYST_SOLUTION;

@Configuration
class StrategyConfiguration {

  @Bean
  CurrentStrategyProvider currentStrategyProvider() {
    return new FixedStrategyProvider(USE_ANALYST_SOLUTION);
  }
}
