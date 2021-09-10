package com.silenteight.payments.bridge.ae.domain.alertregistration.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.domain.alertregistration.port.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.domain.alertregistration.port.RegisterAlertResponse;
import com.silenteight.payments.bridge.ae.domain.alertregistration.port.RegisterAlertUseCase;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RegisterAlertService implements RegisterAlertUseCase {

  @Override
  public RegisterAlertResponse registerAlert(RegisterAlertRequest request) {
    return null;
  }
}
