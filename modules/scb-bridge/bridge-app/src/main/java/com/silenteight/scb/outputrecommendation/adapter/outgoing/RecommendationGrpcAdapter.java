package com.silenteight.scb.outputrecommendation.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.recommendation.api.library.v1.RecommendationLibraryException;
import com.silenteight.recommendation.api.library.v1.RecommendationServiceClient;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations;
import com.silenteight.scb.outputrecommendation.domain.port.outgoing.RecommendationApiClient;

import io.vavr.control.Try;
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
    return Try
        .of(() -> recommendationServiceClient.getRecommendations(
            RecommendationGrpcMapper.toRequest(analysisName, alertNames)))
        .peek(response -> log.trace("Received recommendation {}", response))
        .map(RecommendationGrpcMapper::toResponse)
        .onSuccess(recommendations -> log.info("Received {} recommendations for analysis {}.",
            recommendations.recommendations().size(), analysisName))
        .get();
  }
}
