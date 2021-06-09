package com.silenteight.hsbc.bridge.recommendation;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RecommendationProperties.class)
@RequiredArgsConstructor
class UseCaseConfiguration {

  private final RecommendationProperties recommendationProperties;
  private final RecommendationRepository repository;

  @Bean
  GetRecommendationUseCase getRecommendationUseCase() {
    return new GetRecommendationUseCase(recommendationMapper(), repository);
  }

  private RecommendationMapper recommendationMapper() {
    return new RecommendationMapper(
        recommendationProperties.getSilentEightValues(),
        recommendationProperties.getUserFriendlyValues());
  }
}
