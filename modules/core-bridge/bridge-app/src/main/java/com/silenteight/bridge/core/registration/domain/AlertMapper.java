package com.silenteight.bridge.core.registration.domain;

import com.silenteight.bridge.core.registration.domain.model.Alert;
import com.silenteight.bridge.core.registration.domain.model.AlertsToRegister;
import com.silenteight.bridge.core.registration.domain.model.AlertsToRegister.AlertWithMatches;
import com.silenteight.bridge.core.registration.domain.model.Match;
import com.silenteight.bridge.core.registration.domain.model.Match.Status;
import com.silenteight.bridge.core.registration.domain.model.RegisteredAlerts;

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
            .status(Alert.Status.REGISTERED)
            .alertId(registeredAlert.alertId())
            .batchId(batchId)
            .metadata(getAlertMetadata(successAlerts, registeredAlert.alertId()))
            .matches(registeredAlert.matches().stream()
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

  List<Alert> toErrorAlerts(
      List<RegisterAlertsCommand.AlertWithMatches> failedAlerts, String batchId) {

    return failedAlerts.stream()
        .map(alert -> Alert.builder()
            .alertId(alert.alertId())
            .batchId(batchId)
            .status(Alert.Status.ERROR)
            .metadata(alert.alertMetadata())
            .errorDescription(alert.errorDescription())
            .matches(alert.matches().stream()
                .map(this::toErrorMatch)
                .toList())
            .build())
        .toList();
  }

  private AlertWithMatches toAlertWithMatches(RegisterAlertsCommand.AlertWithMatches alert) {
    return new AlertWithMatches(
        alert.alertId(),
        alert.matches().stream()
            .map(match -> new AlertsToRegister.Match(match.id()))
            .toList());
  }

  private Match toMatch(RegisteredAlerts.Match match) {
    return Match.builder()
        .name(match.name())
        .status(Status.REGISTERED)
        .matchId(match.matchId())
        .build();
  }

  private Match toErrorMatch(RegisterAlertsCommand.Match match) {
    return Match.builder()
        .status(Status.ERROR)
        .matchId(match.id())
        .build();
  }
}