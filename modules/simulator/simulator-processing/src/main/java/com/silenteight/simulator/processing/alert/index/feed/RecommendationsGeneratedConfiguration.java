package com.silenteight.simulator.processing.alert.index.feed;

import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RecommendationsGeneratedConfiguration {

  @Bean
  RecommendationsGeneratedUseCase recommendationsGeneratedUseCase(
      AlertService alertService, IndexedAlertService indexedAlertService) {

    return new RecommendationsGeneratedUseCase(
        alertService, indexedAlertService, new RequestIdGenerator());
  }
}
