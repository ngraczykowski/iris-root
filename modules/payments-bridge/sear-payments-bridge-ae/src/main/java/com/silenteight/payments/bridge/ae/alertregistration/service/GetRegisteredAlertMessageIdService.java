package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.GetRegisteredAlertMessageIdUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisteredAlertDataAccessPort;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class GetRegisteredAlertMessageIdService implements GetRegisteredAlertMessageIdUseCase {

  private final RegisteredAlertDataAccessPort registeredAlertDataAccessPort;

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  public UUID getAlertMessageId(String alertName) {
    return registeredAlertDataAccessPort.getAlertMessageId(alertName);
  }
}
