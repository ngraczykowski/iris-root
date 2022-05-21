package com.silenteight.hsbc.bridge.recommendation;

import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
class RecommendationGeneratorConfiguration {

  private final RecommendationRepository repository;
  private final ApplicationEventPublisher eventPublisher;

  @Bean
  RecommendationGenerator recommendationGenerator() {
    return new RecommendationGenerator(eventPublisher);
  }

  @Bean
  RecommendationServiceClientMock recommendationServiceApiMock() {
    return new RecommendationServiceClientMock(repository);
  }
}
