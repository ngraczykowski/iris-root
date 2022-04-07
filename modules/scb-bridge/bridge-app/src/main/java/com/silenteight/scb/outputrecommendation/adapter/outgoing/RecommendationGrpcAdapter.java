package com.silenteight.scb.outputrecommendation.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.recommendation.api.library.v1.RecommendationServiceClient;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations;
import com.silenteight.scb.outputrecommendation.domain.port.outgoing.RecommendationApiClient;

import io.vavr.control.Try;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
class RecommendationGrpcAdapter implements RecommendationApiClient {

  private final RecommendationServiceClient recommendationServiceClient;

  @Override
  public Recommendations getRecommendations(String analysisName, List<String> alertNames) {
    return Try
        .of(() -> recommendationServiceClient.getRecommendations(
            RecommendationGrpcMapper.toRequest(analysisName, alertNames)))
        .peek(response -> log.info("Received recommendation {}", response))
        .map(RecommendationGrpcMapper::toResponse)
        .onSuccess(recommendations -> log.info("Received {} recommendations for analysis {}.",
            recommendations.recommendations().size(), analysisName))
        .get();
  }
}
