package com.silenteight.scb.ingest.infrastructure.grpc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.registration.api.library.v1.*;

import java.util.List;

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
        .registeredAlertWithMatches(List.of(
            RegisteredAlertWithMatchesOut.builder()
                .alertId("someAlertId")
                .alertName("someAlertName")
                .alertStatus(AlertStatusOut.SUCCESS)
                .registeredMatches(List.of(
                    RegisteredMatchOut.builder()
                        .matchId("someRegisteredMatchId")
                        .matchName("someRegisteredMatchName")
                        .build()))
                .build()))
        .build();
  }
}
