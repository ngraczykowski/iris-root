package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.RegisterAlertsCommand.AlertStatus;
import com.silenteight.bridge.core.registration.domain.model.Alert;
import com.silenteight.bridge.core.registration.domain.model.AlertWithMatches;
import com.silenteight.bridge.core.registration.domain.model.RegistrationAlert;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRegistrationService;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
class AlertService {

  private final AlertMapper alertMapper;
  private final AlertRepository alertRepository;
  private final AlertRegistrationService alertRegistrationService;
  private final RegistrationAlertResponseMapper registrationAlertResponseMapper;

  List<AlertWithMatches> getAlertsAndMatches(String batchId) {
    return alertRepository.findAllWithMatchesByBatchId(batchId);
  }

  List<RegistrationAlert> registerAlertsAndMatches(RegisterAlertsCommand command) {
    var batchId = command.batchId();
    var alertIds = command.alertWithMatches().stream()
        .map(RegisterAlertsCommand.AlertWithMatches::alertId)
        .toList();
    var alreadyRegisteredAlerts =
        alertRepository.findAllWithMatchesByBatchIdAndAlertIdsIn(command.batchId(), alertIds);
    var newAlerts = filterOutExistingInDb(
        command.alertWithMatches(), alreadyRegisteredAlerts);
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
      log.info(
          "Failed alerts saved for batchId: {}, alertCount: {}",
          batchId, failedAlerts.size());
    }

    if (CollectionUtils.isNotEmpty(alreadyRegisteredAlerts)) {
      log.info(
          "Alerts already registered in db for batchId: {}, alertCount: {}",
          batchId, alreadyRegisteredAlerts.size());
    }

    return Stream.of(
            registrationAlertResponseMapper.fromAlertsToRegistrationAlerts(failedAlerts),
            registrationAlertResponseMapper.fromAlertsToRegistrationAlerts(registeredAlerts),
            registrationAlertResponseMapper.fromAlertsWithMatchesToRegistrationAlerts(
                alreadyRegisteredAlerts)
        )
        .flatMap(Collection::stream)
        .toList();
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
      List<RegisterAlertsCommand.AlertWithMatches> alertWithMatches,
      List<AlertWithMatches> existingAlerts) {

    if (CollectionUtils.isNotEmpty(existingAlerts)) {
      return alertWithMatches.stream()
          .filter(alert -> existingAlerts.stream()
              .noneMatch(existing -> alert.alertId().equals(existing.id())))
          .toList();
    }
    return alertWithMatches;
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
    var alertsToRegister = alertMapper.toAlertsToRegister(successAlerts);
    var registeredAlerts = alertRegistrationService.registerAlerts(alertsToRegister);
    return alertMapper.toAlerts(registeredAlerts, successAlerts, batchId);
  }

  private List<Alert> getFailedAlerts(
      List<RegisterAlertsCommand.AlertWithMatches> alertsWithMatches, String batchId) {
    var failedAlerts = filterAlertsByStatus(alertsWithMatches, AlertStatus.FAILURE);
    return alertMapper.toErrorAlerts(failedAlerts, batchId);
  }
}
