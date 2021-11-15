package com.silenteight.payments.bridge.app.integration.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.event.RecommendationCompletedEvent;
import com.silenteight.payments.bridge.event.RecommendationCompletedEvent.AdjudicationRecommendationCompletedEvent;
import com.silenteight.payments.bridge.event.RecommendationCompletedEvent.BridgeRecommendationCompletedEvent;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageUseCase;
import com.silenteight.payments.bridge.warehouse.index.port.IndexBridgeRecommendationUseCase;
import com.silenteight.payments.bridge.warehouse.index.port.IndexRecommendationUseCase;

import org.springframework.core.annotation.Order;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

@MessageEndpoint
@RequiredArgsConstructor
class IndexRecommendationCompletedEndpoint {

  private final AlertMessageUseCase alertMessageUseCase;
  private final IndexBridgeRecommendationUseCase indexBridgeRecommendationUseCase;
  private final IndexRecommendationUseCase indexRecommendationUseCase;

  @Order(1)
  @ServiceActivator(inputChannel = RecommendationCompletedEvent.CHANNEL)
  void accept(RecommendationCompletedEvent event) {
    var alertData = alertMessageUseCase.findByAlertMessageId(event.getAlertId());

    if (event instanceof AdjudicationRecommendationCompletedEvent) {
      var aeEvent = (AdjudicationRecommendationCompletedEvent) event;
      indexRecommendationUseCase.index(alertData, aeEvent.getRecommendation());
    } else {
      var bridgeEvent = (BridgeRecommendationCompletedEvent) event;
      indexBridgeRecommendationUseCase.index(alertData,
          bridgeEvent.getStatus(), bridgeEvent.getReason());
    }
  }
}
