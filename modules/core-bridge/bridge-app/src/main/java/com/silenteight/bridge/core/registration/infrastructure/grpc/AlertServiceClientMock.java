package com.silenteight.bridge.core.registration.infrastructure.grpc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.alert.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Slf4j
class AlertServiceClientMock implements AlertServiceClient {

  @Override
  public RegisterAlertsAndMatchesOut registerAlertsAndMatches(RegisterAlertsAndMatchesIn command) {
    log.info("MOCK: Create alerts and matches in AE.");
    return RegisterAlertsAndMatchesOut.builder()
        .alertWithMatches(command.getAlertsWithMatches().stream()
            .map(this::getAlertWithMatches)
            .toList())
        .build();
  }

  private AlertWithMatchesOut getAlertWithMatches(BatchRegisterAlertMatchesIn alert) {
    var alertName = "alerts/" + UUID.randomUUID();
    return AlertWithMatchesOut.builder()
        .alertId(alert.getAlertId())
        .alertName(alertName)
        .matches(getAlertMatches(alert.getMatchIds(), alertName))
        .build();
  }

  private List<AlertMatchOut> getAlertMatches(Collection<String> matches, String alertName) {
    return matches.stream()
        .map(matchId -> AlertMatchOut.builder()
            .matchId(matchId)
            .name(alertName + "/matches/" + UUID.randomUUID())
            .build())
        .toList();
  }
}
