package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.GetRegisteredAlertSystemIdUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisteredAlertDataAccessPort;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class GetRegisteredAlertSystemIdService implements GetRegisteredAlertSystemIdUseCase {

  private final RegisteredAlertDataAccessPort registeredAlertDataAccessPort;

  @Override
  public String getAlertSystemId(String alertName) {
    return registeredAlertDataAccessPort.getAlertSystemId(alertName);
  }
}
