package com.silenteight.simulator.processing.alert.index.amqp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RecommendationsGeneratedConfiguration {

  @Bean
  RecommendationsGeneratedUseCase recommendationsGeneratedUseCase() {
    return new RecommendationsGeneratedUseCase();
  }
}
