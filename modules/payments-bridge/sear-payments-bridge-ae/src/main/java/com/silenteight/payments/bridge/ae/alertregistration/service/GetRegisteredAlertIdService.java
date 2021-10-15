package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.GetRegisteredAlertIdUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisteredAlertDataAccessPort;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class GetRegisteredAlertIdService implements GetRegisteredAlertIdUseCase {

  private final RegisteredAlertDataAccessPort registeredAlertDataAccessPort;

  @Override
  public String getAlertId(String alertName) {
    return registeredAlertDataAccessPort.getAlertId(alertName);
  }
}
