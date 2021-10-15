package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.ae.alertregistration.port.RegisteredAlertDataAccessPort;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
class CreateRegisteredAlertUseCase {

  private final RegisteredAlertDataAccessPort registeredAlertDataAccessPort;

  void save(UUID alertId, String alertName) {
    log.info("Register alert, alertId ={}, alertName = {}", alertId, alertName);
    registeredAlertDataAccessPort.save(alertId, alertName);
  }
}
