package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.ae.alertregistration.port.DeleteRegisteredAlertUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisteredAlertDataAccessPort;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class DeleteRegisteredAlertService implements DeleteRegisteredAlertUseCase {

  private final RegisteredAlertDataAccessPort registeredAlertDataAccessPort;

  @Override
  @Timed(percentiles = {0.5, 0.95, 0.99}, histogram = true)
  public List<String> delete(List<String> alertNames) {

    log.info(
        "Deleting registered messages from mapping table: alertCount={}, alerts={}",
        alertNames.size(), alertNames);

    var registeredAlerts = registeredAlertDataAccessPort.delete(alertNames);

    log.info("Registered alerts mappings removed, count={}", registeredAlerts.size());

    return registeredAlerts;
  }


}
