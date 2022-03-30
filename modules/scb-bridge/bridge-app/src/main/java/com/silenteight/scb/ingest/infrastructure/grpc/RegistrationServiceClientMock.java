package com.silenteight.scb.ingest.infrastructure.grpc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.registration.api.library.v1.*;

@Slf4j
class RegistrationServiceClientMock implements RegistrationServiceClient {

  @Override
  public EmptyOut registerBatch(RegisterBatchIn request) {
    log.info("MOCK: Batch with id: {} registered in Core Bridge.", request.getBatchId());
    return EmptyOut.getInstance();
  }

  @Override
  public EmptyOut notifyBatchError(NotifyBatchErrorIn request) {
    log.info("MOCK: Batch with id: {} status set to ERROR in Core Bridge", request.getBatchId());
    return EmptyOut.getInstance();
  }

  @Override
  public RegisterAlertsAndMatchesOut registerAlertsAndMatches(RegisterAlertsAndMatchesIn request) {
    log.info(
        "MOCK: Alerts and matches for batch id: {} registered in Core Bridge",
        request.getBatchId());

    return RegisterAlertsAndMatchesOut.builder()
        .registeredAlertWithMatches(request.getAlertsWithMatches().stream()
            .map(alert -> RegisteredAlertWithMatchesOut.builder()
                .alertId(alert.getAlertId())
                .alertName("someAlertName")
                .alertStatus(AlertStatusOut.SUCCESS)
                .registeredMatches(alert.getMatches().stream()
                    .map(match -> RegisteredMatchOut.builder()
                        .matchId(match.getMatchId())
                        .matchName("someMatchName")
                        .build())
                    .toList())
                .build())
            .toList())
        .build();
  }
}