package com.silenteight.bridge.core.registration.adapter.outgoing;

import com.silenteight.adjudication.api.library.v1.alert.*;
import com.silenteight.bridge.core.registration.domain.model.AlertsToRegister;
import com.silenteight.bridge.core.registration.domain.model.RegisteredAlerts;
import com.silenteight.bridge.core.registration.domain.model.RegisteredAlerts.AlertWithMatches;
import com.silenteight.bridge.core.registration.domain.model.RegisteredAlerts.Match;

import org.springframework.stereotype.Component;

@Component
class AlertRegistrationMapper {

  RegisterAlertsAndMatchesIn toRequest(AlertsToRegister alertsToRegister) {
    return RegisterAlertsAndMatchesIn.builder()
        .alertsWithMatches(alertsToRegister.registerAlertsWithMatches().stream()
            .map(this::toBatchCreateAlertMatchesIn)
            .toList())
        .build();
  }

  RegisteredAlerts toRegisteredAlerts(RegisterAlertsAndMatchesOut result) {
    return new RegisteredAlerts(
        result.getAlertWithMatches().stream()
            .map(this::toAlertWithMatches)
            .toList());
  }

  private BatchRegisterAlertMatchesIn toBatchCreateAlertMatchesIn(
      AlertsToRegister.AlertWithMatches alertWithMatches) {
    return BatchRegisterAlertMatchesIn.builder()
        .alertId(alertWithMatches.alertId())
        .alertPriority(alertWithMatches.priority())
        .matchIds(alertWithMatches.matches().stream()
            .map(AlertsToRegister.Match::matchId)
            .toList())
        .build();
  }

  private AlertWithMatches toAlertWithMatches(AlertWithMatchesOut alertWithMatchesOut) {
    return AlertWithMatches.builder()
        .alertId(alertWithMatchesOut.getAlertId())
        .name(alertWithMatchesOut.getAlertName())
        .matches(alertWithMatchesOut.getMatches().stream()
            .map(this::toMatch)
            .toList())
        .build();
  }

  private Match toMatch(AlertMatchOut matchOut) {
    return new Match(matchOut.getMatchId(), matchOut.getName());
  }
}
