package com.silenteight.scb.ingest.adapter.incomming.common.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.domain.payload.PayloadConverter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class ScbRecommendationServiceConfiguration {

  private final ScbRecommendationRepository scbRecommendationRepository;
  private final PayloadConverter payloadConverter;

  @Bean
  ScbRecommendationService scbRecommendationService() {
    return new ScbRecommendationService(
        scbRecommendationRepository,
        new ScbDiscriminatorMatcher(),
        payloadConverter);
  }
}
