package com.silenteight.hsbc.bridge.recommendation;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class RecommendationFacadeConfiguration {

  private final RecommendationRepository recommendationRepository;

  @Bean
  RecommendationFacade recommendationFacade() {
    return new RecommendationFacade(recommendationRepository);
  }
}
