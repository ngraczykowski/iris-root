package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.core.alertmessage.port.IssueRecommendationUseCase;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

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

  @Transactional
  @Override
  public void issue(UUID alertMessageId) {
    var alertStatusEntity = alertMessageStatusRepository
        .findByAlertMessageId(alertMessageId).orElseThrow(EntityNotFoundException::new);
    if (!alertStatusEntity.getStatus().isTransitionAllowed(ACCEPTED)) {
      // the received event seems to be obsolete. Skip gracefully.
      log.debug("The received event is already solved. Skipping further processing. AlertId={}",
          alertMessageId);
      return;
    }

    if (isRequiredResolutionTimeElapsed(alertStatusEntity.getLastModifyAt())) {
      responseGeneratorService.prepareAndSendResponse(alertMessageId, REJECTED_OUTDATED);
      log.info("The required resolution time elapsed. Reject outdated alert-message. AlertId={}",
          alertMessageId);
      transitionAlertMessageStatusService
          .transitionAlertMessageStatus(alertMessageId, REJECTED_OUTDATED);
      return;
    }

    responseGeneratorService.prepareAndSendResponse(alertMessageId, RECOMMENDED);
    transitionAlertMessageStatusService.transitionAlertMessageStatus(alertMessageId, RECOMMENDED);
  }

  private boolean isRequiredResolutionTimeElapsed(OffsetDateTime enteredStatusAt) {
    return enteredStatusAt.plus(alertMessageProperties.getIssueDecisionRequestedTime())
        .compareTo(OffsetDateTime.now(clock)) >= 0;
  }

}
