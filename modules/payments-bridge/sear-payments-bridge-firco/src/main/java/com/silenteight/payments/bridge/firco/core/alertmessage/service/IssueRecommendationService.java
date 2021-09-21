package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.core.alertmessage.port.IssueRecommendationUseCase;
import com.silenteight.payments.common.resource.ResourceName;
import com.silenteight.proto.payments.bridge.internal.v1.event.MessageStored;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;
import javax.persistence.EntityNotFoundException;

import static com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus.ACCEPTED;
import static com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus.RECOMMENDED;
import static com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus.REJECTED_OUTDATED;

@EnableConfigurationProperties(AlertMessageProperties.class)
@RequiredArgsConstructor
@Slf4j
@Service
class IssueRecommendationService implements IssueRecommendationUseCase {

  private final AlertMessageProperties alertMessageProperties;

  private final AlertMessageStatusRepository alertMessageStatusRepository;
  private final TransitionAlertMessageStatusService transitionAlertMessageStatusService;
  private final ResponseGeneratorService responseGeneratorService;

  @Setter
  private Clock clock = Clock.systemUTC();

  @Override
  public void issue(MessageStored messageStored) {
    var alertMessageId = ResourceName.create(messageStored.getAlert()).getUuid("alert-messages");
    var alertStatusEntity = alertMessageStatusRepository
        .findByAlertMessageId(alertMessageId).orElseThrow(EntityNotFoundException::new);

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

    responseGeneratorService.prepareAndSendResponse(alertMessageId, RECOMMENDED);
    transitionAlertMessageStatusService.transitionAlertMessageStatus(alertMessageId, RECOMMENDED);
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
        .plus(alertMessageProperties.getIssueDecisionRequestedTime())
        .compareTo(OffsetDateTime.now(clock)) <= 0;
    var alertMessageId = alertMessageStatus.getAlertMessageId();
    if (isOverdue) {
      responseGeneratorService.prepareAndSendResponse(alertMessageId, REJECTED_OUTDATED);
      transitionAlertMessageStatusService
          .transitionAlertMessageStatus(alertMessageId, REJECTED_OUTDATED);
      return true;
    }
    return false;
  }

}
