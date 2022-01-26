package com.silenteight.bridge.core.registration.domain;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.model.Alert;
import com.silenteight.bridge.core.registration.domain.model.AlertStatus;
import com.silenteight.bridge.core.registration.domain.model.AlertWithMatches;
import com.silenteight.bridge.core.registration.domain.model.RegistrationAlert;
import com.silenteight.bridge.core.registration.domain.model.RegistrationAlert.RegistrationMatch;
import com.silenteight.bridge.core.registration.domain.model.RegistrationAlert.Status;

import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
class RegistrationAlertResponseMapper {

  List<RegistrationAlert> fromAlertsWithMatchesToRegistrationAlerts(
      List<AlertWithMatches> alerts) {
    return alerts.stream()
        .map(alert -> RegistrationAlert.builder()
            .id(alert.id())
            .name(alert.name())
            .status(toStatus(alert.status()))
            .matches(createRegistrationMatchesFromAlertWithMatches(alert))
            .build())
        .toList();
  }

  List<RegistrationAlert> fromAlertsToRegistrationAlerts(List<Alert> alerts) {
    return alerts.stream()
        .map(alert -> RegistrationAlert.builder()
            .id(alert.alertId())
            .name(alert.name())
            .status(toStatus(alert.status()))
            .matches(createRegistrationMatchesFromAlerts(alert))
            .build())
        .toList();
  }

  private List<RegistrationMatch> createRegistrationMatchesFromAlertWithMatches(
      AlertWithMatches alert) {
    return alert.matches().stream()
        .map(match -> RegistrationMatch.builder()
            .id(match.id())
            .name(match.name())
            .build())
        .toList();
  }

  private List<RegistrationMatch> createRegistrationMatchesFromAlerts(Alert alert) {
    return alert.matches().stream()
        .map(match -> RegistrationMatch.builder()
            .id(match.matchId())
            .name(match.name())
            .build())
        .toList();
  }

  private Status toStatus(AlertStatus status) {
    return switch (status) {
      case REGISTERED, PROCESSING, RECOMMENDED -> Status.SUCCESS;
      case ERROR -> Status.FAILURE;
    };
  }
}
