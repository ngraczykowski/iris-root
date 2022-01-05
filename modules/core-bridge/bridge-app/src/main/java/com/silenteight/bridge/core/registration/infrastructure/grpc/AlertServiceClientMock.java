package com.silenteight.bridge.core.registration.infrastructure.grpc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.alert.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Slf4j
class AlertServiceClientMock implements AlertServiceClient {

  @Override
  public BatchCreateAlertsOut batchCreateAlerts(List<AlertIn> alerts) {
    log.info("MOCK: Create alerts in AE.");
    return BatchCreateAlertsOut.builder()
        .alerts(List.of(
            AlertOut.builder()
                .name("alertName1")
                .alertId("1")
                .build(),
            AlertOut.builder()
                .name("alertName2")
                .alertId("2")
                .build(),
            AlertOut.builder()
                .name("alertName3")
                .alertId("3")
                .build()
        ))
        .build();
  }

  @Override
  public BatchCreateAlertMatchesOut batchCreateAlertMatches(BatchCreateAlertMatchesIn command) {
    log.info("MOCK: Create alert matches in AE.");
    return BatchCreateAlertMatchesOut.builder()
        .alertMatches(List.of(
            AlertMatchOut.builder()
                .name("matchName1")
                .matchId("11")
                .build(),
            AlertMatchOut.builder()
                .name("matchName2")
                .matchId("22")
                .build(),
            AlertMatchOut.builder()
                .name("matchName3")
                .matchId("33")
                .build()
        ))
        .build();
  }

  @Override
  public RegisterAlertsAndMatchesOut registerAlertsAndMatches(RegisterAlertsAndMatchesIn command) {
    log.info("MOCK: Create alerts and matches in AE.");
    return RegisterAlertsAndMatchesOut.builder()
        .alertWithMatches(command.getAlertsWithMatches().stream()
            .map(this::getAlertWithMatches)
            .toList())
        .build();
  }

  private AlertWithMatchesOut getAlertWithMatches(BatchCreateAlertMatchesIn alert) {
    return AlertWithMatchesOut.builder()
        .alertId(alert.getAlertId())
        .alertName(UUID.randomUUID().toString())
        .matches(getAlertMatches(alert.getMatchIds()))
        .build();
  }

  private List<AlertMatchOut> getAlertMatches(Collection<String> matches) {
    return matches.stream()
        .map(matchId -> AlertMatchOut.builder()
            .matchId(matchId)
            .name(UUID.randomUUID().toString())
            .build())
        .toList();
  }
}
