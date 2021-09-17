package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.UUID;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
class TransitionAlertMessageStatusService {

  private final AlertMessageStatusRepository repository;

  @Setter
  private Clock clock = Clock.systemUTC();

  @Transactional
  public void transitionAlertMessageStatus(
      UUID alertMessageId, AlertMessageStatus destinationStatus) {

    var entity = repository
        .findByAlertMessageId(alertMessageId)
        .orElseThrow();
    entity.transitionStatusOrElseThrow(destinationStatus, clock);
    if (log.isTraceEnabled()) {
      log.trace("AlertMessage [{}] transited to {}", alertMessageId, destinationStatus.name());
    }
    repository.save(entity);
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
