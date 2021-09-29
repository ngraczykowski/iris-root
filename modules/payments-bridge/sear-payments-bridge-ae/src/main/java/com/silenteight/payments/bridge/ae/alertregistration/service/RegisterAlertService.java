package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
class RegisterAlertService implements RegisterAlertUseCase {

  private final CreateAlertsService createAlertsService;

  public List<RegisterAlertResponse> register(List<RegisterAlertRequest> registerAlertRequest) {
    var registeredAlertMatches = new ArrayList<RegisterAlertResponse>();
    registerAlertRequest.forEach(
        ar -> registeredAlertMatches.add(createAlertsService.createAlert(ar)));
    return registeredAlertMatches;
  }

}
