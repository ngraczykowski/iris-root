package com.silenteight.payments.bridge.app.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.model.AlertData;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageUseCase;
import com.silenteight.payments.bridge.firco.recommendation.model.AdjudicationEngineSourcedRecommendation;
import com.silenteight.payments.bridge.firco.recommendation.model.BridgeSourcedRecommendation;
import com.silenteight.payments.bridge.firco.recommendation.port.IndexRecommendationPort;
import com.silenteight.payments.bridge.warehouse.index.model.IndexRecommendationRequest;
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
    var request = createRequest(recommendation, alertData);
    indexRecommendationUseCase.index(request);
  }

  // TODO(wkeska): Move this code to AlertData after it will be moved to firco module
  private static IndexRecommendationRequest createRequest(
      AdjudicationEngineSourcedRecommendation recommendation, AlertData alertData) {
    return IndexRecommendationRequest
        .builder()
        .discriminator(alertData.getDiscriminator())
        .systemId(alertData.getSystemId())
        .recommendationWithMetadata(recommendation.getRecommendation())
        .build();
  }

  @Override
  public void send(BridgeSourcedRecommendation recommendation) {
    var alertData = alertMessageUseCase.findByAlertMessageId(recommendation.getAlertId());
    indexBridgeRecommendationUseCase.index(recommendation.toIndexRecommendationRequest(alertData));
  }
}
