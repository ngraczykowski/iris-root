package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.core.alertmessage.port.TransitionAlertMessageStatusUseCase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.UUID;

@RequiredArgsConstructor
@Service
class TransitionAlertMessageStatusService implements TransitionAlertMessageStatusUseCase {

  private final AlertMessageStatusRepository repository;

  @Setter
  private Clock clock = Clock.systemUTC();

  @Transactional
  @Override
  public void transitionAlertMessageStatus(
      UUID alertMessageId, AlertMessageStatus destinationStatus) {

    var entity = repository
        .findByAlertMessageId(alertMessageId)
        .orElseGet(() -> new AlertMessageStatusEntity(alertMessageId))
        .transitionStatus(destinationStatus, clock);

    repository.save(entity);
  }
}
