package com.silenteight.simulator.processing.alert.index.feed;

import com.silenteight.simulator.processing.alert.index.amqp.gateway.SimulationDataIndexRequestGateway;
import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ProcessingFeedProperties.class)
class RecommendationsGeneratedConfiguration {

  @Bean
  RecommendationsGeneratedUseCase recommendationsGeneratedUseCase(
      IndexedAlertService indexedAlertService,
      SimulationDataIndexRequestGateway simulationDataIndexRequestGateway,
      ProcessingFeedProperties properties) {

    return new RecommendationsGeneratedUseCase(
        indexedAlertService,
        new RequestIdGenerator(),
        simulationDataIndexRequestGateway,
        properties.getBatchSize());
  }
}
