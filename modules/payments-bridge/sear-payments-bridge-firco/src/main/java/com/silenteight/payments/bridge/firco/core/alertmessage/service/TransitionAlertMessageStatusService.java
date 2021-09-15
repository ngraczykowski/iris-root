package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.UUID;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
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
    repository.save(entity);
  }

  @Transactional
  public void initState(UUID alertMessageId) {
    var entity = repository.findByAlertMessageId(alertMessageId);
    if (entity.isPresent()) {
      throw new IllegalStateException("Unable to re-initialize the initial state for alertID: " +
          alertMessageId);
    }
    repository.save(new AlertMessageStatusEntity(alertMessageId));
  }

}
