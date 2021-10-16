package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.event.RecommendationCompletedEvent;
import com.silenteight.payments.bridge.firco.alertmessage.port.FilterAlertMessageUseCase;
import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationId;
import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationWrapper;
import com.silenteight.payments.bridge.firco.recommendation.port.CreateRecommendationUseCase;
import com.silenteight.payments.bridge.firco.recommendation.port.NotifyResponseCompletedUseCase;
import com.silenteight.sep.base.aspects.logging.LogContext;

import org.slf4j.MDC;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.RECOMMENDATION_COMPLETED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.RECOMMENDED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus.PENDING;

@RequiredArgsConstructor
@Slf4j
@MessageEndpoint
class RecommendationCompletedEndpoint {

  private final AlertMessageStatusService alertMessageStatusService;
  private final FilterAlertMessageUseCase alertMessageUseCase;
  private final NotifyResponseCompletedUseCase notifyResponseCompletedUseCase;
  private final CreateRecommendationUseCase createRecommendationUseCase;

  @ServiceActivator(inputChannel = RECOMMENDATION_COMPLETED)
  @LogContext
  void accept(RecommendationCompletedEvent event) {
    var alertId = event.getAlertId();
    MDC.put("alertId", alertId.toString());
    event.getRecommendation().ifPresent(r ->
        MDC.put("recommendation", r.getRecommendation().getRecommendedAction()));

    var recommendation = saveRecommendation(event);
    if (alertMessageUseCase.isResolvedOrOutdated(event)) {
      return;
    }

    log.info("Alert recommendation completed.");

    notifyResponseCompletedUseCase.notify(alertId, recommendation.getId(), RECOMMENDED);
    alertMessageStatusService.transitionAlertMessageStatus(alertId, RECOMMENDED, PENDING);
  }

  private RecommendationId saveRecommendation(RecommendationCompletedEvent event) {
    var alertId = event.getAlertId();
    var wrapper = event
        .getRecommendation()
        .map(r -> new RecommendationWrapper(alertId, r))
        .orElseGet(() -> new RecommendationWrapper(alertId));

    return createRecommendationUseCase.createRecommendation(wrapper);
  }
}
