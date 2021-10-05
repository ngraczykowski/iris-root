package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@EnableConfigurationProperties(AlertMessageProperties.class)
@RequiredArgsConstructor
@Service
@Slf4j
class AlertMessageStatusService {

  private final AlertMessageStatusRepository repository;
  private final AlertMessagePayloadRepository payloadRepository;
  private final AlertMessageProperties alertMessageProperties;

  @Setter
  private Clock clock = Clock.systemUTC();

  AlertMessageStatusEntity findByAlertId(UUID alertMessageId) {
    return repository.findByAlertMessageId(alertMessageId)
        .orElseThrow(EntityNotFoundException::new);
  }

  /**
   * Transition alert to the required status.
   * If the destinationStatus is final, the method assumes that it was achieved
   * after delivering response to the requesting party.
   */
  @Transactional
  public void transitionAlertMessageStatus(
      UUID alertMessageId, AlertMessageStatus destinationStatus) {
    transitionAlertMessageStatus(alertMessageId, destinationStatus,
        destinationStatus.isFinal() ? DeliveryStatus.DELIVERED : DeliveryStatus.NA);
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
    entity.transitionStatusOrElseThrow(destinationStatus, clock, delivery);
    if (log.isTraceEnabled()) {
      log.trace("AlertMessage [{}] transited to {}", alertMessageId, destinationStatus.name());
    }

    repository.save(entity);

    if (destinationStatus.isFinal() &&
        alertMessageProperties.isOriginalMessageDeletedAfterRecommendation()) {
      payloadRepository.deleteByAlertMessageId(alertMessageId);
    }
  }

  @Transactional
  public void initState(UUID alertMessageId) {
    var entity = repository.findByAlertMessageId(alertMessageId);
    if (entity.isPresent()) {
      throw new IllegalStateException("Unable to re-initialize the initial state for alertID: " +
          alertMessageId);
    }
    if (log.isTraceEnabled()) {
      log.trace("AlertMessage [{}] transited to RECEIVED", alertMessageId);
    }
    repository.save(new AlertMessageStatusEntity(alertMessageId));
  }

}
