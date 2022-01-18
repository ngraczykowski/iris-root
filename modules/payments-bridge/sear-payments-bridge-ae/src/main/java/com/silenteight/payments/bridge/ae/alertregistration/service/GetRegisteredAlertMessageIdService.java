package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.GetRegisteredAlertMessageIdUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisteredAlertDataAccessPort;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class GetRegisteredAlertMessageIdService implements GetRegisteredAlertMessageIdUseCase {

  private final RegisteredAlertDataAccessPort registeredAlertDataAccessPort;

  @Override
  public UUID getAlertMessageId(String alertName) {
    return registeredAlertDataAccessPort.getAlertMessageId(alertName);
  }
}
