package com.silenteight.simulator.processing.alert.index.feed;

import com.silenteight.simulator.management.domain.SimulationService;
import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RecommendationsGeneratedConfiguration {

  @Bean
  RecommendationsGeneratedUseCase recommendationsGeneratedUseCase(
      SimulationService simulationService,
      RecommendationService recommendationService,
      IndexedAlertService indexedAlertService) {

    return new RecommendationsGeneratedUseCase(
        simulationService, recommendationService, indexedAlertService, new RequestIdGenerator());
  }
}
