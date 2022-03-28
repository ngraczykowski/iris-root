package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageStatusUseCase;
import com.silenteight.sep.base.aspects.metrics.Timed;

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
class AlertMessageStatusService implements AlertMessageStatusUseCase {

  private final AlertMessageStatusRepository repository;
  private final AlertMessagePayloadRepository payloadRepository;
  private final AlertMessageProperties alertMessageProperties;

  @Setter
  private Clock clock = Clock.systemUTC();

  @Timed(percentiles = {0.5, 0.95, 0.99}, histogram = true)
  public AlertMessageStatusEntity findByAlertId(UUID alertMessageId) {
    return repository.findByAlertMessageId(alertMessageId)
        .orElseThrow(EntityNotFoundException::new);
  }

  @Override
  @Timed(percentiles = {0.5, 0.95, 0.99}, histogram = true)
  public AlertMessageStatus getStatus(UUID alertMessageId) {
    return findByAlertId(alertMessageId).getStatus();
  }

  /**
   * Transition alert to the required status.
   */
  @Transactional
  @Timed(percentiles = {0.5, 0.95, 0.99}, histogram = true)
  public boolean transitionAlertMessageStatus(
      UUID alertMessageId, AlertMessageStatus destinationStatus, DeliveryStatus delivery) {

    var entity = repository
        .findByAlertMessageIdAndLockForWrite(alertMessageId)
        .orElseThrow();

    var result = entity.transitionStatus(destinationStatus, delivery, clock);
    if (result != TransitionResult.SUCCESS) {
      return false;
    }


    repository.save(entity);

    if (destinationStatus.isFinal() && delivery.isFinal() &&
        alertMessageProperties.isOriginalMessageDeletedAfterRecommendation()) {
      log.debug("Removing original message of alert: {}", alertMessageId);
      payloadRepository.deleteByAlertMessageId(alertMessageId);
    }

    log.info("Alert [{}] transitioned to {}. Delivery status: {}", alertMessageId,
        destinationStatus, delivery);
    return true;
  }

  @Transactional
  @Timed(percentiles = {0.5, 0.95, 0.99}, histogram = true)
  public void initState(UUID alertMessageId) {
    var entity = repository.findByAlertMessageId(alertMessageId);
    if (entity.isPresent()) {
      throw new IllegalStateException("Unable to re-initialize the initial state for alertID: " +
          alertMessageId);
    }
    repository.save(new AlertMessageStatusEntity(alertMessageId));
    log.debug("Alert [{}] initialized with RECEIVED", alertMessageId);
  }

}
