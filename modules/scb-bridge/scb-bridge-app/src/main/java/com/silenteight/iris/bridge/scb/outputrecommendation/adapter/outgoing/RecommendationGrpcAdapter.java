/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.port.outgoing.RecommendationApiClient;
import com.silenteight.recommendation.api.library.v1.RecommendationLibraryException;
import com.silenteight.recommendation.api.library.v1.RecommendationServiceClient;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
class RecommendationGrpcAdapter implements RecommendationApiClient {

  private final RecommendationServiceClient recommendationServiceClient;

  @Override
  @Retryable(value = RecommendationLibraryException.class,
      maxAttemptsExpression = "${grpc.client.retry.max-attempts}",
      backoff = @Backoff(
          multiplierExpression = "${grpc.client.retry.multiplier}",
          delayExpression = "${grpc.client.retry.delay-in-milliseconds}"))
  public Recommendations getRecommendations(String analysisName, List<String> alertNames) {
    var recommendations =
        recommendationServiceClient.getRecommendations(
            RecommendationGrpcMapper.toRequest(analysisName, alertNames));
    log.trace("Received recommendation {}", recommendations);
    var response = RecommendationGrpcMapper.toResponse(recommendations);
    log.info("Received {} recommendations for analysis {}.",
        response.recommendations().size(), analysisName);
    return response;
  }
}
