package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlert;
import com.silenteight.payments.bridge.ae.alertregistration.port.AlertRegisteredAccessPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.FindRegisteredAlertUseCase;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class FindAlertRegisteredServiceAlert implements FindRegisteredAlertUseCase {

  private final AlertRegisteredAccessPort alertRegisteredAccessPort;

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public List<RegisteredAlert> find(List<String> registeredAlert) {
    return alertRegisteredAccessPort.findRegistered(registeredAlert);
  }

}
