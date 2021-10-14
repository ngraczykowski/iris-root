package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.payments.bridge.event.AlertRejectedEvent;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.messaging.support.MessageBuilder;
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
class AlertMessageStatusService {

  private final AlertMessageStatusRepository repository;
  private final AlertMessagePayloadRepository payloadRepository;
  private final AlertMessageProperties alertMessageProperties;
  private final CommonChannels commonChannels;

  @Setter
  private Clock clock = Clock.systemUTC();

  AlertMessageStatusEntity findByAlertId(UUID alertMessageId) {
    return repository.findByAlertMessageId(alertMessageId)
        .orElseThrow(EntityNotFoundException::new);
  }

  /**
   * Transition alert to the required status.
   */
  @Transactional
  public void transitionAlertMessageStatus(
      UUID alertMessageId, AlertMessageStatus destinationStatus, DeliveryStatus delivery) {

    var entity = repository
        .findByAlertMessageIdAndLockForWrite(alertMessageId)
        .orElseThrow();
    entity.transitionStatusOrElseThrow(destinationStatus, delivery, clock);
    log.info("Alert [{}] is transited to {}. Delivery status: {}", alertMessageId,
          destinationStatus, delivery);

    repository.save(entity);

    if (destinationStatus.isFinal() &&
        alertMessageProperties.isOriginalMessageDeletedAfterRecommendation()) {
      log.debug("Removing original message of alert: {}", alertMessageId);
      payloadRepository.deleteByAlertMessageId(alertMessageId);
    }

    publishRejectionIfApply(alertMessageId, destinationStatus);
  }

  private void publishRejectionIfApply(UUID alertId, AlertMessageStatus destinationStatus) {
    if (EnumSet.of(REJECTED_OVERFLOWED, REJECTED_DAMAGED, REJECTED_OUTDATED)
        .contains(destinationStatus)) {
      commonChannels.alertRejected().send(
          MessageBuilder.withPayload(
              new AlertRejectedEvent(alertId, destinationStatus.name())).build()
      );
    }
  }

  @Transactional
  public void initState(UUID alertMessageId) {
    var entity = repository.findByAlertMessageId(alertMessageId);
    if (entity.isPresent()) {
      throw new IllegalStateException("Unable to re-initialize the initial state for alertID: " +
          alertMessageId);
    }
    if (log.isDebugEnabled()) {
      log.debug("AlertMessage [{}] transited to RECEIVED", alertMessageId);
    }
    repository.save(new AlertMessageStatusEntity(alertMessageId));
  }

}
