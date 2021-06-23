package com.silenteight.hsbc.bridge.recommendation;

import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class HandlerConfiguration {

  private final ApplicationEventPublisher eventPublisher;
  private final RecommendationServiceClient recommendationServiceClient;
  private final RecommendationRepository recommendationRepository;

  @Bean
  RecommendationEventListener recommendationEventListener() {
    return new RecommendationEventListener(recommendationHandler());
  }

  @Bean
  RecommendationHandler recommendationHandler() {
    return new RecommendationHandler(
        recommendationRepository, recommendationServiceClient, eventPublisher);
  }
}
