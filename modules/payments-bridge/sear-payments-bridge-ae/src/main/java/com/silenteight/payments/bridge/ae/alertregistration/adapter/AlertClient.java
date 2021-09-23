package com.silenteight.payments.bridge.ae.alertregistration.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesRequest;
import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesResponse;
import com.silenteight.adjudication.api.v1.CreateAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.AlertClientPort;

import io.grpc.Deadline;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
class AlertClient implements AlertClientPort {

  private final AlertServiceBlockingStub stub;

  private final Duration timeout;

  public Alert createAlert(CreateAlertRequest request) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    if (log.isTraceEnabled()) {
      log.trace("Requesting creating alert: deadline={}, request={}", deadline, request);
    }

    return stub.withDeadline(deadline).createAlert(request);
  }

  public BatchCreateAlertMatchesResponse createMatches(BatchCreateAlertMatchesRequest request) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    if (log.isTraceEnabled()) {
      log.trace("Requesting adding matches to alert: deadline={}, request={}", deadline, request);
    }

    return stub.batchCreateAlertMatches(request);
  }
}
