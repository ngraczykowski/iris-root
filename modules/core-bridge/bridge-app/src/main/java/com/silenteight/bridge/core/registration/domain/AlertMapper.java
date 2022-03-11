package com.silenteight.bridge.core.registration.domain;

import com.silenteight.bridge.core.registration.domain.command.RegisterAlertsCommand;
import com.silenteight.bridge.core.registration.domain.model.*;
import com.silenteight.bridge.core.registration.domain.model.AlertsToRegister.AlertWithMatches;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
class AlertMapper {

  AlertsToRegister toAlertsToRegister(List<RegisterAlertsCommand.AlertWithMatches> alerts) {
    return new AlertsToRegister(alerts.stream()
        .map(this::toAlertWithMatches)
        .toList());
  }

  List<Alert> toAlerts(
      RegisteredAlerts registeredAlerts,
      List<RegisterAlertsCommand.AlertWithMatches> successAlerts,
      String batchId) {
    return registeredAlerts.registeredAlertsWithMatches().stream()
        .map(registeredAlert -> Alert.builder()
            .name(registeredAlert.name())
            .status(AlertStatus.REGISTERED)
            .alertId(registeredAlert.alertId())
            .batchId(batchId)
            .metadata(getAlertMetadata(successAlerts, registeredAlert.alertId()))
            .matches(registeredAlert.matches().stream()
                .map(this::toMatch)
                .toList())
            .build())
        .toList();
  }

  List<Alert> toErrorAlerts(
      List<RegisterAlertsCommand.AlertWithMatches> failedAlerts, String batchId) {

    return failedAlerts.stream()
        .map(alert -> Alert.builder()
            .alertId(alert.alertId())
            .batchId(batchId)
            .status(AlertStatus.ERROR)
            .metadata(alert.alertMetadata())
            .errorDescription(alert.errorDescription())
            .matches(alert.matches().stream()
                .map(this::toMatch)
                .toList())
            .build())
        .toList();
  }

  private String getAlertMetadata(
      List<RegisterAlertsCommand.AlertWithMatches> successAlerts, String alertId) {
    return successAlerts.stream()
        .filter(alertWithMatches -> alertId.equals(alertWithMatches.alertId()))
        .findAny()
        .map(RegisterAlertsCommand.AlertWithMatches::alertMetadata)
        .orElseThrow();
  }

  private AlertWithMatches toAlertWithMatches(RegisterAlertsCommand.AlertWithMatches alert) {
    return new AlertWithMatches(
        alert.alertId(),
        alert.matches().stream()
            .map(match -> new AlertsToRegister.Match(match.id()))
            .toList());
  }

  private Match toMatch(RegisteredAlerts.Match match) {
    return new Match(match.name(), match.matchId());
  }

  private Match toMatch(RegisterAlertsCommand.Match match) {
    return new Match(null, match.id());
  }
}
