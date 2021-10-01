package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.event.RecommendationCompletedEvent;
import com.silenteight.payments.bridge.firco.callback.port.CreateResponseUseCase;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.RECOMMENDATION_COMPLETED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.ACCEPTED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.RECOMMENDED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.REJECTED_OUTDATED;

@EnableConfigurationProperties(AlertMessageProperties.class)
@RequiredArgsConstructor
@Slf4j
@MessageEndpoint
class RecommendationReceivedEndpoint {

  private final AlertMessageProperties alertMessageProperties;

  private final AlertMessageStatusService alertMessageStatusService;
  private final CreateResponseUseCase createResponseUseCase;

  @Setter
  private Clock clock = Clock.systemUTC();

  @ServiceActivator(inputChannel = RECOMMENDATION_COMPLETED)
  public void accept(RecommendationCompletedEvent recommendation) {
    var alertId = UUID.fromString(recommendation.getRecommendation().getAlert());
    var alertStatusEntity = alertMessageStatusService.findByAlertId(alertId);

    if (isTransitionForbidden(alertStatusEntity)) {
      return;
    }

    if (isRequiredResolutionTimeElapsed(alertStatusEntity)) {
      return;
    }

    /*
     * The request isn't sent to AE at version one, thus the artificial transition from
     * (RECEIVED, STORED) to (RECOMMENDED) has been enabled.
     */
    createResponseUseCase.createResponse(alertId, RECOMMENDED);
    alertMessageStatusService.transitionAlertMessageStatus(alertId, RECOMMENDED);
  }

  private boolean isTransitionForbidden(AlertMessageStatusEntity alertMessageStatus) {
    if (!alertMessageStatus.getStatus().isTransitionAllowed(ACCEPTED)) {
      // the received event seems to be obsolete. Skip gracefully.
      log.debug("The AlertMessage [{}] is already solved (status={}). Skipping further processing.",
          alertMessageStatus.getAlertMessageId(), alertMessageStatus.getStatus().name());
      return true;
    }
    return false;
  }

  private boolean isRequiredResolutionTimeElapsed(AlertMessageStatusEntity alertMessageStatus) {
    var isOverdue = alertMessageStatus.getLastModifyAt()
        .plus(alertMessageProperties.getDecisionRequestedTime())
        .compareTo(OffsetDateTime.now(clock)) <= 0;
    var alertMessageId = alertMessageStatus.getAlertMessageId();
    if (isOverdue) {
      createResponseUseCase.createResponse(alertMessageId, REJECTED_OUTDATED);
      alertMessageStatusService
          .transitionAlertMessageStatus(alertMessageId, REJECTED_OUTDATED);
      return true;
    }
    return false;
  }

}
