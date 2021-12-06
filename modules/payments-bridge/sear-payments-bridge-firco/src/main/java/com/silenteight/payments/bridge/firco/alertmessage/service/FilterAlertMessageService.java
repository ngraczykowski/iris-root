package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.model.AlertData;
import com.silenteight.payments.bridge.common.model.AlertId;
import com.silenteight.payments.bridge.firco.alertmessage.port.FilterAlertMessageUseCase;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.ACCEPTED;

@Service
@EnableConfigurationProperties(AlertMessageProperties.class)
@RequiredArgsConstructor
@Slf4j
class FilterAlertMessageService implements FilterAlertMessageUseCase {

  private final AlertMessageProperties alertMessageProperties;
  private final AlertMessageStatusService alertMessageStatusService;
  private final AlertMessageService alertMessageService;
  @Setter private Clock clock = Clock.systemUTC();

  @Override
  public boolean isOutdated(AlertId alert) {
    var status = alertMessageStatusService.findByAlertId(alert.getAlertId());
    return isRequiredResolutionTimeElapsed(status);
  }

  @Override
  public boolean isResolvedOrOutdated(AlertId alert) {
    var status = alertMessageStatusService.findByAlertId(alert.getAlertId());
    return isTransitionForbidden(status) || isRequiredResolutionTimeElapsed(status);
  }

  @Override
  public boolean hasTooManyHits(AlertId alert) {
    return isMaxHitsPerAlertExceeded(alert);
  }

  private static boolean isTransitionForbidden(AlertMessageStatusEntity alertMessageStatus) {
    if (!alertMessageStatus.getStatus().isTransitionAllowed(ACCEPTED)) {
      // the received event seems to be obsolete. Skip gracefully.
      log.debug("The AlertMessage [{}] is already solved (status={}). Skipping further processing.",
          alertMessageStatus.getAlertMessageId(), alertMessageStatus.getStatus().name());
      return true;
    }
    return false;
  }

  private boolean isRequiredResolutionTimeElapsed(AlertMessageStatusEntity alertMessageStatus) {
    var isOverdue = alertMessageStatus.getCreatedAt()
        .plus(alertMessageProperties.getDecisionRequestedTime())
        .compareTo(OffsetDateTime.now(clock)) <= 0;
    if (isOverdue) {
      log.debug(
          "The AlertMessage [{}] is outdated. Skipping further processing.",
          alertMessageStatus.getAlertMessageId());
      // simply stop processing and let the RejectingOutdated process finishes (don't interfere).
      return true;
    }
    return false;
  }

  private boolean isMaxHitsPerAlertExceeded(AlertId alert) {
    UUID alertId = alert.getAlertId();
    Integer numberOfHits = Optional.of(alertId)
        .map(alertMessageService::findByAlertMessageId)
        .map(AlertData::getNumberOfHits)
        .orElse(0);

    if (alertMessageProperties.getMaxHitsPerAlert() < numberOfHits) {
      log.info(
          "The AlertMessage [{}] has too many hits [{}]. Responding with MANUAL_INVESTIGATION",
          alertId, numberOfHits);
      return true;
    }
    return false;
  }
}
