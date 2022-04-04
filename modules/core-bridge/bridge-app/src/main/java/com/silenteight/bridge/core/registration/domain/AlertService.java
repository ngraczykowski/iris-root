package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.command.RegisterAlertsCommand;
import com.silenteight.bridge.core.registration.domain.command.RegisterAlertsCommand.AlertStatus;
import com.silenteight.bridge.core.registration.domain.model.Alert;
import com.silenteight.bridge.core.registration.domain.model.AlertWithMatches;
import com.silenteight.bridge.core.registration.domain.model.Batch;
import com.silenteight.bridge.core.registration.domain.model.RegistrationAlert;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRegistrationService;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
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

  List<RegistrationAlert> registerAlertsAndMatches(
      RegisterAlertsCommand command, Integer priority) {
    var batchId = command.batchId();
    var alertIds = getAlertIds(command);

    var alreadyRegisteredAlertsWithMatches =
        alertRepository.findAllWithMatchesByBatchIdAndAlertIdsIn(command.batchId(), alertIds);
    var newAlerts = filterOutExistingInDb(
        command.alertWithMatches(), alreadyRegisteredAlertsWithMatches, batchId);

    var alreadyRegistered = getAlreadyRegistered(alreadyRegisteredAlertsWithMatches);
    var successfulAlerts = registerSuccessful(batchId, newAlerts, priority);
    var failedAlerts = saveFailed(batchId, newAlerts);

    return Stream.of(successfulAlerts, failedAlerts, alreadyRegistered)
        .flatMap(Collection::stream)
        .toList();
  }

  private List<RegistrationAlert> getAlreadyRegistered(
      List<AlertWithMatches> alreadyRegisteredAlerts) {
    return registrationAlertResponseMapper.fromAlertsWithMatchesToRegistrationAlerts(
        alreadyRegisteredAlerts);
  }

  private List<String> getAlertIds(RegisterAlertsCommand command) {
    return command.alertWithMatches().stream()
        .map(RegisterAlertsCommand.AlertWithMatches::alertId)
        .toList();
  }

  private List<RegistrationAlert> saveFailed(
      String batchId, List<RegisterAlertsCommand.AlertWithMatches> newAlerts) {
    var failedAlerts = getFailedAlerts(newAlerts, batchId);
    if (CollectionUtils.isNotEmpty(failedAlerts)) {
      alertRepository.saveAlerts(failedAlerts);
      log.info(
          "Failed alerts saved for batchId: {}, alertCount: {}",
          batchId, failedAlerts.size());
      return registrationAlertResponseMapper.fromAlertsToRegistrationAlerts(failedAlerts);
    }
    return Collections.emptyList();
  }

  private List<RegistrationAlert> registerSuccessful(
      String batchId, List<RegisterAlertsCommand.AlertWithMatches> newAlerts, Integer priority) {
    var successfulAlerts = getSuccessful(newAlerts);
    if (CollectionUtils.isNotEmpty(successfulAlerts)) {
      var alertsRegisteredInAE = register(successfulAlerts, batchId, priority);
      log.info(
          "Alerts registered in AE for batchId: {}, alertCount: {}", batchId,
          alertsRegisteredInAE.size());
      alertRepository.saveAlerts(alertsRegisteredInAE);
      log.info(
          "Registered alerts saved for batchId: {}, alertCount: {}", batchId,
          alertsRegisteredInAE.size());
      return registrationAlertResponseMapper.fromAlertsToRegistrationAlerts(alertsRegisteredInAE);
    }
    return Collections.emptyList();
  }

  void updateStatusToRecommended(String batchId, List<String> alertNames) {
    log.info("Update alerts with names {} for batch id: {}", alertNames, batchId);
    alertRepository.updateStatusToRecommended(batchId, alertNames);
  }

  boolean hasNoPendingAlerts(Batch batch) {
    var completedAlertsCount = alertRepository.countAllCompleted(batch.id());
    log.info(
        "{} alerts completed (RECOMMENDED, ERROR, DELIVERED), {} alerts count for batch with id {}",
        completedAlertsCount, batch.alertsCount(), batch.id());

    return completedAlertsCount == batch.alertsCount();
  }

  void updateStatusToDelivered(String batchId, List<String> alertNames) {
    if (CollectionUtils.isEmpty(alertNames)) {
      log.info("Update all alerts status to DELIVERED for batchId: {}", batchId);
      alertRepository.updateStatusToDelivered(batchId);
    } else {
      log.info("Update {} alerts status to DELIVERED for batchId: {}", alertNames.size(), batchId);
      alertRepository.updateStatusToDelivered(batchId, alertNames);
    }
  }

  boolean hasAllDeliveredAlerts(Batch batch) {
    return batch.alertsCount() == alertRepository.countAllDeliveredAndErrorAlerts(batch.id());
  }

  private List<RegisterAlertsCommand.AlertWithMatches> filterOutExistingInDb(
      List<RegisterAlertsCommand.AlertWithMatches> alertWithMatches,
      List<AlertWithMatches> existingAlerts, String batchId) {

    if (CollectionUtils.isNotEmpty(existingAlerts)) {
      log.info(
          "Alerts already stored in db for batchId: {}, alertCount: {}", batchId,
          existingAlerts.size());
      return alertWithMatches.stream()
          .filter(alert -> existingAlerts.stream()
              .noneMatch(existing -> alert.alertId().equals(existing.id())))
          .toList();
    }
    return alertWithMatches;
  }

  private List<RegisterAlertsCommand.AlertWithMatches> getSuccessful(
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
      List<RegisterAlertsCommand.AlertWithMatches> successAlerts, String batchId,
      Integer priority) {
    var alertsToRegister = alertMapper.toAlertsToRegister(successAlerts, priority);
    var registeredAlerts = alertRegistrationService.registerAlerts(alertsToRegister);
    return alertMapper.toAlerts(registeredAlerts, successAlerts, batchId);
  }

  private List<Alert> getFailedAlerts(
      List<RegisterAlertsCommand.AlertWithMatches> alertsWithMatches, String batchId) {
    var failedAlerts = filterAlertsByStatus(alertsWithMatches, AlertStatus.FAILURE);
    return alertMapper.toErrorAlerts(failedAlerts, batchId);
  }
}
