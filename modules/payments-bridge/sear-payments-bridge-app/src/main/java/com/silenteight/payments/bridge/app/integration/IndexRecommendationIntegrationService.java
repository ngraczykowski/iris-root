package com.silenteight.payments.bridge.app.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageUseCase;
import com.silenteight.payments.bridge.firco.recommendation.model.AdjudicationEngineSourcedRecommendation;
import com.silenteight.payments.bridge.firco.recommendation.model.BridgeSourcedRecommendation;
import com.silenteight.payments.bridge.firco.recommendation.port.IndexRecommendationPort;
import com.silenteight.payments.bridge.warehouse.index.port.IndexBridgeRecommendationUseCase;
import com.silenteight.payments.bridge.warehouse.index.port.IndexRecommendationUseCase;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class IndexRecommendationIntegrationService implements IndexRecommendationPort {

  private final AlertMessageUseCase alertMessageUseCase;
  private final IndexBridgeRecommendationUseCase indexBridgeRecommendationUseCase;
  private final IndexRecommendationUseCase indexRecommendationUseCase;

  @Override
  public void send(AdjudicationEngineSourcedRecommendation recommendation) {
    var alertData = alertMessageUseCase.findByAlertMessageId(recommendation.getAlertId());
    indexRecommendationUseCase.index(alertData, recommendation.getRecommendation());
  }

  @Override
  public void send(BridgeSourcedRecommendation recommendation) {
    var alertData = alertMessageUseCase.findByAlertMessageId(recommendation.getAlertId());
    indexBridgeRecommendationUseCase.index(alertData,
        recommendation.getStatus(), recommendation.getReason());
  }
}
