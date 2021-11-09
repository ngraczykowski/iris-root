package com.silenteight.payments.bridge.data.retention.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.data.retention.adapter.AlertDataRetentionAccessPort;
import com.silenteight.payments.bridge.data.retention.model.AlertDataRetention;
import com.silenteight.payments.bridge.data.retention.port.CreateAlertDataRetentionUseCase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class CreateAlertDataRetentionService implements CreateAlertDataRetentionUseCase {

  private final AlertDataRetentionAccessPort alertDataRetentionAccessPort;

  @Override
  @Transactional
  public void create(Iterable<AlertDataRetention> alertRetention) {
    alertDataRetentionAccessPort.insert(alertRetention);
  }

}
