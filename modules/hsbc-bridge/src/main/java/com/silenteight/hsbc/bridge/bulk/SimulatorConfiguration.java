package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.recommendation.RecommendationGenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
class SimulatorConfiguration {

  private final BulkRepository bulkRepository;
  private final RecommendationGenerator recommendationGenerator;

  @Bean
  BatchProcessingSimulator batchProcessingSimulator() {
    return new BatchProcessingSimulator(bulkRepository, recommendationGenerator);
  }
}
