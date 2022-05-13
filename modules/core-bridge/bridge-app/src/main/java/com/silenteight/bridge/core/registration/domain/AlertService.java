package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.AlertWithoutMatches;
import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.MatchWithAlertId;
import com.silenteight.bridge.core.registration.domain.command.RegisterAlertsCommand;
import com.silenteight.bridge.core.registration.domain.model.*;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRegistrationService;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository;
import com.silenteight.bridge.core.registration.domain.strategy.BatchStrategyFactory;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
class AlertService {

  private final AlertMapper alertMapper;
  private final AlertRepository alertRepository;
  private final AlertRegistrationService alertRegistrationService;
  private final RegistrationAlertResponseMapper registrationAlertResponseMapper;
  private final BatchStrategyFactory batchStrategyFactory;

  List<AlertWithMatches> getAlertsAndMatches(String batchId) {
    return alertRepository.findAllWithMatchesByBatchId(batchId);
  }

  List<AlertWithMatches> getAlertsAndMatchesByName(String batchId, List<String> alertNames) {
    return alertRepository.findAllWithMatchesByBatchIdAndNameIn(batchId, alertNames);
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

  Stream<AlertWithoutMatches> streamAllByBatchId(String batchId) {
    return alertRepository.streamAllByBatchId(batchId);
  }

  Stream<AlertWithoutMatches> streamAllByBatchIdAndNameIn(String batchId, List<String> alertNames) {
    return alertRepository.streamAllByBatchIdAndNameIn(batchId, alertNames);
  }

  List<MatchWithAlertId> getAllMatchesForAlerts(Set<Long> alertsIds) {
    return alertRepository.findMatchesByAlertIdIn(alertsIds);
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
          "Failed alerts saved for batch id [{}], alert count [{}].", batchId, failedAlerts.size());
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
          "Alerts registered in AE for batch id [{}], alert count [{}].",
          batchId, alertsRegisteredInAE.size());
      alertRepository.saveAlerts(alertsRegisteredInAE);
      log.info(
          "Registered alerts saved for batch id [{}], alert count [{}].",
          batchId, alertsRegisteredInAE.size());
      return registrationAlertResponseMapper.fromAlertsToRegistrationAlerts(alertsRegisteredInAE);
    }
    return Collections.emptyList();
  }

  void updateStatusToRecommended(String batchId, List<String> alertNames) {
    log.info("Update alerts with names [{}] for batch id [{}].", alertNames, batchId);
    alertRepository.updateStatusToRecommended(batchId, alertNames);
  }

  boolean hasNoPendingAlerts(Batch batch) {
    return batchStrategyFactory.getStrategyForPendingAlerts(batch).hasNoPendingAlerts(batch);
  }

  void updateStatusToDelivered(String batchId, List<String> alertNames) {
    if (CollectionUtils.isEmpty(alertNames)) {
      log.info(
          "Update all alerts status to [{}] for batch id [{}].", AlertStatus.DELIVERED, batchId);
      alertRepository.updateStatusToDelivered(batchId);
    } else {
      log.info(
          "Update [{}] alerts status to [{}] for batch id [{}].",
          alertNames.size(), AlertStatus.DELIVERED, batchId);
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
          "Alerts already stored in db for batch id [{}], alert count [{}].",
          batchId, existingAlerts.size());
      return alertWithMatches.stream()
          .filter(alert -> existingAlerts.stream()
              .noneMatch(existing -> alert.alertId().equals(existing.id())))
          .toList();
    }
    return alertWithMatches;
  }

  private List<RegisterAlertsCommand.AlertWithMatches> getSuccessful(
      List<RegisterAlertsCommand.AlertWithMatches> alertsWithMatches) {
    return filterAlertsByStatus(alertsWithMatches, RegisterAlertsCommand.AlertStatus.SUCCESS);
  }

  private List<RegisterAlertsCommand.AlertWithMatches> filterAlertsByStatus(
      List<RegisterAlertsCommand.AlertWithMatches> alertsWithMatches,
      RegisterAlertsCommand.AlertStatus status) {
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
    var failedAlerts =
        filterAlertsByStatus(alertsWithMatches, RegisterAlertsCommand.AlertStatus.FAILURE);
    return alertMapper.toErrorAlerts(failedAlerts, batchId);
  }
}
