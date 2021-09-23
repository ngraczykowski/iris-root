package com.silenteight.payments.bridge.ae.alertregistration.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.adjudication.api.v1.BatchCreateAlertsRequest;
import com.silenteight.adjudication.api.v1.BatchCreateAlertsResponse;
import com.silenteight.adjudication.api.v1.BatchCreateMatchesRequest;
import com.silenteight.adjudication.api.v1.BatchCreateMatchesResponse;
import com.silenteight.payments.bridge.ae.alertregistration.port.AlertClientPort;

import io.grpc.Deadline;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
class AlertClient implements AlertClientPort {

  private final AlertServiceBlockingStub stub;

  private final Duration timeout;

  public BatchCreateAlertsResponse createAlert(BatchCreateAlertsRequest request) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    if (log.isTraceEnabled()) {
      log.trace("Requesting creating alert: deadline={}, request={}", deadline, request);
    }

    return stub.withDeadline(deadline).batchCreateAlerts(request);
  }

  public BatchCreateMatchesResponse createMatches(BatchCreateMatchesRequest request) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    if (log.isTraceEnabled()) {
      log.trace("Requesting adding matches to alert: deadline={}, request={}", deadline, request);
    }

    return stub.batchCreateMatches(request);
  }
}
