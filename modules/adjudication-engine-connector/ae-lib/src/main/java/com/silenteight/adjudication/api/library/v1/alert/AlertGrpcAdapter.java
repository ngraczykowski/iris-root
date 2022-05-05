package com.silenteight.adjudication.api.library.v1.alert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.AdjudicationEngineLibraryRuntimeException;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.adjudication.api.v1.BatchCreateAlertsRequest;

import io.vavr.control.Try;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class AlertGrpcAdapter implements AlertServiceClient {

  private static final String CANNOT_CREATE_BATCH_ALERTS = "Cannot create batch alerts";
  private static final String CANNOT_CREATE_BATCH_ALERT_MATCHES =
      "Cannot create batch alert matches";
  private final AlertServiceBlockingStub alertServiceBlockingStub;
  private final long deadlineInSeconds;

  @Override
  public RegisterAlertsAndMatchesOut registerAlertsAndMatches(RegisterAlertsAndMatchesIn command) {
    var registerAlertsRequest = BatchCreateAlertsRequest.newBuilder()
        .addAllAlerts(command.getAlertsWithMatches().stream()
            .map(AlertGrpcMapper::toAlert)
            .collect(Collectors.toList()))
        .build();

    return Try.of(() -> getStub().batchCreateAlerts(registerAlertsRequest))
        .map(AlertGrpcMapper::toRegisterAlertsAndMatchesOut)
        .onFailure(e -> log.error(CANNOT_CREATE_BATCH_ALERTS, e))
        .onSuccess(result -> log.debug("Batch created alerts successfully"))
        .getOrElseThrow(e -> new AdjudicationEngineLibraryRuntimeException(
            CANNOT_CREATE_BATCH_ALERT_MATCHES, e));
  }

  private AlertServiceBlockingStub getStub() {
    return alertServiceBlockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
