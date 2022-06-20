/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.domain.payload.PayloadConverter;

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
