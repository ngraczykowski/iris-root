package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.event.AlertRejectedEvent;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageStatusUseCase;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertRejectedPublisherPort;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.EnumSet;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.REJECTED_DAMAGED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.REJECTED_OUTDATED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.REJECTED_OVERFLOWED;

@EnableConfigurationProperties(AlertMessageProperties.class)
@RequiredArgsConstructor
@Service
@Slf4j
class AlertMessageStatusService implements AlertMessageStatusUseCase {

  private final AlertMessageStatusRepository repository;
  private final AlertMessagePayloadRepository payloadRepository;
  private final AlertMessageProperties alertMessageProperties;
  private final AlertRejectedPublisherPort alertRejectedPublisherPort;

  @Setter
  private Clock clock = Clock.systemUTC();

  AlertMessageStatusEntity findByAlertId(UUID alertMessageId) {
    return repository.findByAlertMessageId(alertMessageId)
        .orElseThrow(EntityNotFoundException::new);
  }

  @Override
  public AlertMessageStatus getStatus(UUID alertMessageId) {
    return findByAlertId(alertMessageId).getStatus();
  }

  /**
   * Transition alert to the required status.
   */
  @Transactional
  public boolean transitionAlertMessageStatus(
      UUID alertMessageId, AlertMessageStatus destinationStatus, DeliveryStatus delivery) {

    var entity = repository
        .findByAlertMessageIdAndLockForWrite(alertMessageId)
        .orElseThrow();

    var result = entity.transitionStatus(destinationStatus, delivery, clock);
    if (result != TransitionResult.SUCCESS) {
      return false;
    }

    log.info("Alert [{}] transitioned to {}. Delivery status: {}", alertMessageId,
        destinationStatus, delivery);

    repository.save(entity);

    if (destinationStatus.isFinal() && delivery.isFinal() &&
        alertMessageProperties.isOriginalMessageDeletedAfterRecommendation()) {
      log.debug("Removing original message of alert: {}", alertMessageId);
      payloadRepository.deleteByAlertMessageId(alertMessageId);
    }

    publishRejectionIfApply(alertMessageId, destinationStatus);
    return true;
  }

  private void publishRejectionIfApply(UUID alertId, AlertMessageStatus destinationStatus) {
    if (EnumSet.of(REJECTED_OVERFLOWED, REJECTED_DAMAGED, REJECTED_OUTDATED)
        .contains(destinationStatus)) {
      alertRejectedPublisherPort.send(new AlertRejectedEvent(alertId, destinationStatus.name()));
    }
  }

  @Transactional
  public void initState(UUID alertMessageId) {
    var entity = repository.findByAlertMessageId(alertMessageId);
    if (entity.isPresent()) {
      throw new IllegalStateException("Unable to re-initialize the initial state for alertID: " +
          alertMessageId);
    }

    log.debug("Alert [{}] initialized with RECEIVED", alertMessageId);

    repository.save(new AlertMessageStatusEntity(alertMessageId));
  }

}
