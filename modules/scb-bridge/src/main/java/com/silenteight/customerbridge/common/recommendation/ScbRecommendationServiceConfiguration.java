package com.silenteight.customerbridge.common.recommendation;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class ScbRecommendationServiceConfiguration {

  private final ScbRecommendationRepository scbRecommendationRepository;
  private final DiscriminatorFetcher discriminatorFetcher;

  @Bean
  ScbRecommendationService scbRecommendationService() {
    return new ScbRecommendationService(
        scbRecommendationRepository,
        discriminatorFetcher,
        new ScbDiscriminatorMatcher());
  }
}
