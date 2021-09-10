package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.core.alertmessage.port.CountAlertMessagesInStatusUseCase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
class CountAlertMessagesInStatusService implements CountAlertMessagesInStatusUseCase {

  private final AlertMessageStatusRepository repository;

  @Transactional(readOnly = true)
  @Override
  public long countAlertMessagesInStatus(AlertMessageStatus status) {
    return repository.countAllByStatus(status);
  }
}
