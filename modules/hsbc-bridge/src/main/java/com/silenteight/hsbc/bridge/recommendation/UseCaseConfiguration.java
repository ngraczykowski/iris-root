package com.silenteight.hsbc.bridge.recommendation;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RecommendationProperties.class)
@RequiredArgsConstructor
class UseCaseConfiguration {

  private final RecommendationProperties recommendationProperties;
  private final RecommendationRepository repository;
  private final ObjectMapper objectMapper;

  @Bean
  GetRecommendationUseCase getRecommendationUseCase() {
    return new GetRecommendationUseCase(recommendationMapper(), repository, objectMapper);
  }

  @Bean
  StoreRecommendationsUseCase storeRecommendationsUseCase() {
    return new StoreRecommendationsUseCase(objectMapper, repository);
  }

  private RecommendationMapper recommendationMapper() {
    return new RecommendationMapper(
        recommendationProperties.getSilentEightValues(),
        recommendationProperties.getUserFriendlyValues());
  }
}
