package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.RegisterAlertsCommand.AlertStatus;
import com.silenteight.bridge.core.registration.domain.model.Alert;
import com.silenteight.bridge.core.registration.domain.model.AlertWithMatches;
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

  List<AlertWithMatches> getAlertsAndMatches(String batchId) {
    return alertRepository.findAllWithMatchesByBatchId(batchId);
  }

  void registerAlertsAndMatches(RegisterAlertsCommand command) {
    var batchId = command.batchId();
    var newAlerts = filterOutExistingInDb(command);
    var successAlerts = getSucceededAlerts(newAlerts);
    var registeredAlerts = register(successAlerts, batchId);

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

  void updateStatusToRecommended(String batchId, List<String> alertNames) {
    log.info("Update alerts with names {} for batch id: {}", alertNames, batchId);
    alertRepository.updateStatusToRecommended(batchId, alertNames);
  }

  boolean hasNoPendingAlerts(String batchId) {
    var pendingAlerts = alertRepository.countAllPendingAlerts(batchId);
    log.info("{} alerts left to be recommended for the batch id: {}", pendingAlerts, batchId);
    return pendingAlerts == 0;
  }

  private List<RegisterAlertsCommand.AlertWithMatches> filterOutExistingInDb(
      RegisterAlertsCommand command) {
    var alertIds = command.alertWithMatches().stream()
        .map(RegisterAlertsCommand.AlertWithMatches::alertId)
        .toList();
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

  private List<RegisterAlertsCommand.AlertWithMatches> getSucceededAlerts(
      List<RegisterAlertsCommand.AlertWithMatches> alertsWithMatches) {
    return filterAlertsByStatus(alertsWithMatches, AlertStatus.SUCCESS);
  }

  private List<RegisterAlertsCommand.AlertWithMatches> filterAlertsByStatus(
      List<RegisterAlertsCommand.AlertWithMatches> alertsWithMatches, AlertStatus status) {
    return alertsWithMatches.stream()
        .filter(alert -> status.equals(alert.alertStatus()))
        .toList();
  }

  private List<Alert> register(
      List<RegisterAlertsCommand.AlertWithMatches> successAlerts, String batchId) {
    var alertsToRegister = mapper.toAlertsToRegister(successAlerts);
    var registeredAlerts = alertRegistrationService.registerAlerts(alertsToRegister);
    return mapper.toAlerts(registeredAlerts, successAlerts, batchId);
  }

  private List<Alert> getFailedAlerts(
      List<RegisterAlertsCommand.AlertWithMatches> alertsWithMatches, String batchId) {
    var failedAlerts = filterAlertsByStatus(alertsWithMatches, AlertStatus.FAILURE);
    return mapper.toErrorAlerts(failedAlerts, batchId);
  }
}
