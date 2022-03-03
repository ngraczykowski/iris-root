package com.silenteight.customerbridge.common.recommendation;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class RecommendationServiceConfiguration {

  private final RecommendationOrderProperties recommendationOrderProperties;
  private final ScbRecommendationService scbRecommendationService;

  @Bean
  RecommendationService recommendationService(
      SystemIdFinder systemIdFinder,
      RecommendationOrderHandler recommendationOrderHandler) {
    return RecommendationService.builder()
        .systemIdFinder(systemIdFinder)
        .orderService(recommendationOrderHandler)
        .scbRecommendationService(scbRecommendationService)
        .recommendationOrderProperties(recommendationOrderProperties)
        .build();
  }

}
