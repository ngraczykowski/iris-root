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

  List<Alert> toAlerts(RegisteredAlerts registeredAlerts, String batchId) {
    return registeredAlerts.registeredAlertsWithMatches().stream()
        .map(registeredAlert -> Alert.builder()
            .name(registeredAlert.name())
            .status(Alert.Status.REGISTERED)
            .alertId(registeredAlert.alertId())
            .batchId(batchId)
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
            .status(Alert.Status.ERROR)
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
