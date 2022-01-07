package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.RegisterAlertsCommand.AlertStatus;
import com.silenteight.bridge.core.registration.domain.RegisterAlertsCommand.AlertWithMatches;
import com.silenteight.bridge.core.registration.domain.model.Alert;
import com.silenteight.bridge.core.registration.domain.model.AlertsToRegister;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRegistrationService;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class AlertService {

  private final AlertMapper mapper;
  private final AlertRepository alertRepository;
  private final AlertRegistrationService alertRegistrationService;

  void registerAlertsAndMatches(RegisterAlertsCommand command) {
    var batchId = command.batchId();
    var newAlerts = filterOutExistingInDb(command);
    var successAlerts = getSucceededAlerts(newAlerts);

    var alertsToRegister = mapper.toAlertsToRegister(successAlerts);
    var registeredAlerts = register(alertsToRegister, batchId);
    log.info(
        "Alerts registered in AE for batchId: {}, alertCount: {}",
        batchId, registeredAlerts.size());

    alertRepository.saveAlerts(registeredAlerts);
    log.info("Registered alerts saved for batchId: {}, alertCount: {}",
        batchId, registeredAlerts.size());

    var failedAlerts = getFailedAlerts(newAlerts, batchId);
    if (CollectionUtils.isNotEmpty(failedAlerts)) {
      alertRepository.saveAlerts(failedAlerts);
      log.warn(
          "Failed alerts saved for batchId: {}, alertCount: {}",
          batchId, failedAlerts.size());
    }
  }

  private List<AlertWithMatches> filterOutExistingInDb(RegisterAlertsCommand command) {
    var alertIds = command.alertWithMatches().stream()
        .map(AlertWithMatches::alertId).toList();
    var existingAlerts =
        alertRepository.findAllAlertIdsByBatchIdAndAlertIdIn(command.batchId(), alertIds);

    var result = command.alertWithMatches();
    if (CollectionUtils.isNotEmpty(existingAlerts)) {
      log.info("Alerts already exist in DB. alertCount: {}", existingAlerts.size());
      return result.stream()
          .filter(alert -> existingAlerts.stream()
              .noneMatch(existing -> alert.alertId().equals(existing.alertId())))
          .toList();
    }
    return result;
  }

  private List<AlertWithMatches> getSucceededAlerts(
      List<AlertWithMatches> alertsWithMatches) {

    return filterAlertsByStatus(alertsWithMatches, AlertStatus.SUCCESS);
  }

  private List<AlertWithMatches> filterAlertsByStatus(
      List<AlertWithMatches> alertsWithMatches, AlertStatus status) {

    return alertsWithMatches.stream()
        .filter(alert -> status.equals(alert.alertStatus()))
        .toList();
  }

  private List<Alert> register(AlertsToRegister alertsToRegister, String batchId) {
    var registeredAlerts = alertRegistrationService.registerAlerts(alertsToRegister);
    return mapper.toAlerts(registeredAlerts, batchId);
  }

  private List<Alert> getFailedAlerts(
      List<AlertWithMatches> alertsWithMatches, String batchId) {
    var failedAlerts =
        filterAlertsByStatus(alertsWithMatches, AlertStatus.FAILURE);

    return mapper.toErrorAlerts(failedAlerts, batchId);
  }
}
