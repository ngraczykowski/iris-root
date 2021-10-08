package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.model.AlertId;
import com.silenteight.payments.bridge.firco.alertmessage.port.AcceptAlertMessageUseCase;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;

import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.ACCEPTED;

@Service
@EnableConfigurationProperties(AlertMessageProperties.class)
@RequiredArgsConstructor
@Slf4j
class AcceptAlertMessageService implements AcceptAlertMessageUseCase {

  private final AlertMessageProperties alertMessageProperties;
  private final AlertMessageStatusService alertMessageStatusService;
  @Setter private Clock clock = Clock.systemUTC();

  @Override
  public boolean test(AlertId alert) {
    var status = alertMessageStatusService.findByAlertId(alert.getAlertId());
    return !isTransitionForbidden(status) &&
        !isRequiredResolutionTimeElapsed(status);
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
    if (isOverdue) {
      log.debug("The AlertMessage [{}] is outdated. Skipping further processing.",
          alertMessageStatus.getAlertMessageId());
      // simply stop processing and let the RejectingOutdated process do its job (don't interfere).
      return true;
    }
    return false;
  }

}
