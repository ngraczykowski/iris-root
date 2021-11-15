package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlertWithMatches;
import com.silenteight.payments.bridge.ae.alertregistration.port.AlertRegisteredAccessPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.FindRegisteredUseCase;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class FindAlertRegisteredService implements FindRegisteredUseCase {

  private final AlertRegisteredAccessPort alertRegisteredAccessPort;

  public List<RegisteredAlertWithMatches> find(List<UUID> alertIds) {
    return alertRegisteredAccessPort.findRegistered(alertIds);
  }

}
