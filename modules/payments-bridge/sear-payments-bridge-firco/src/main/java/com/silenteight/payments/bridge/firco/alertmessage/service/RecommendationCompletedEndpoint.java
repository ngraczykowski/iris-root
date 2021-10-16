package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;
import com.silenteight.payments.bridge.event.RecommendationCompletedEvent;
import com.silenteight.payments.bridge.firco.alertmessage.port.FilterAlertMessageUseCase;
import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationId;
import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationWrapper;
import com.silenteight.payments.bridge.firco.recommendation.port.CreateRecommendationUseCase;
import com.silenteight.payments.bridge.firco.recommendation.port.CreateResponseUseCase;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.Optional;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.RECOMMENDATION_COMPLETED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.RECOMMENDED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus.PENDING;

@RequiredArgsConstructor
@Slf4j
@MessageEndpoint
class RecommendationCompletedEndpoint {

  private final AlertMessageStatusService alertMessageStatusService;
  private final FilterAlertMessageUseCase alertMessageUseCase;
  private final CreateResponseUseCase createResponseUseCase;
  private final CreateRecommendationUseCase createRecommendationUseCase;

  @ServiceActivator(inputChannel = RECOMMENDATION_COMPLETED)
  void accept(RecommendationCompletedEvent event) {
    var alertId = event.getAlertId();
    var recommendation = saveRecommendation(event);
    if (alertMessageUseCase.isResolvedOrOutdated(event)) {
      return;
    }

    log.info("Alert recommendation completed: [{}]", alertId);

    /*
     * The request isn't sent to AE at version one, thus the artificial transition from
     * (RECEIVED, STORED) to (RECOMMENDED) has been enabled.
     */
    createResponseUseCase.createResponse(alertId, recommendation.getId(), RECOMMENDED);
    alertMessageStatusService.transitionAlertMessageStatus(alertId, RECOMMENDED, PENDING);
  }

  private RecommendationId saveRecommendation(RecommendationCompletedEvent recommendation) {
    RecommendationWrapper wrapper;
    var alertId = recommendation.getAlertId();
    Optional<RecommendationWithMetadata> recommendationWithMetadata =
        recommendation.getRecommendation();
    if (recommendationWithMetadata.isPresent()) {
      RecommendationWithMetadata actualRecommendation = recommendationWithMetadata.get();

      wrapper = new RecommendationWrapper(alertId, actualRecommendation);
    } else {
      wrapper = new RecommendationWrapper(alertId);
    }

    return createRecommendationUseCase.createRecommendation(wrapper);
  }
}
