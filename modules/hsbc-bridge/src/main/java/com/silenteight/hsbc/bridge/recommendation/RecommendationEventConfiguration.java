package com.silenteight.hsbc.bridge.recommendation;

import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class RecommendationEventConfiguration {

  private final ApplicationEventPublisher eventPublisher;
  private final RecommendationRepository recommendationRepository;

  @Bean
  NewRecommendationEventListener recommendationEventListener() {
    return new NewRecommendationEventListener(eventPublisher, recommendationRepository);
  }
}
