package com.silenteight.payments.bridge.ae.recommendation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.api.v2.GetRecommendationRequest;
import com.silenteight.payments.bridge.ae.recommendation.port.AlertRecommendationValueReceivedGateway;
import com.silenteight.payments.bridge.ae.recommendation.port.ReceiveGeneratedRecommendationUseCase;
import com.silenteight.payments.bridge.ae.recommendation.port.RecommendationClientPort;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
class ReceiveGeneratedGeneratedRecommendationService
    implements ReceiveGeneratedRecommendationUseCase {

  private final RecommendationClientPort recommendationClientPort;
  private final AlertRecommendationValueReceivedGateway alertRecommendationValueReceivedGateway;

  @Override
  public void handleGeneratedRecommendationMessage(RecommendationsGenerated recommendations) {
    recommendations
        .getRecommendationInfosList()
        .stream()
        .map(r -> recommendationClientPort.receiveRecommendation(
            GetRecommendationRequest.newBuilder().setRecommendation(r.getRecommendation()).build()))
        .forEach(alertRecommendationValueReceivedGateway::send);
  }
}
