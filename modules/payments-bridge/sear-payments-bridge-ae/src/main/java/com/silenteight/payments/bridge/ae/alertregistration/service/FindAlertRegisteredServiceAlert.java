package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.FindRegisteredAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlert;
import com.silenteight.payments.bridge.ae.alertregistration.port.AlertRegisteredAccessPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.FindRegisteredAlertUseCase;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class FindAlertRegisteredServiceAlert implements FindRegisteredAlertUseCase {

  private final AlertRegisteredAccessPort alertRegisteredAccessPort;

  public List<RegisteredAlert> find(List<FindRegisteredAlertRequest> registeredAlert) {
    return alertRegisteredAccessPort.findRegistered(registeredAlert);
  }

}
