package com.silenteight.payments.bridge.ae.alertregistration.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesRequest;
import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesResponse;
import com.silenteight.adjudication.api.v1.CreateAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.AlertClientPort;

import com.google.protobuf.TextFormat;
import io.grpc.Deadline;
import io.grpc.StatusRuntimeException;

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

    try {
      var response = stub.withDeadline(deadline).createAlert(request);
      log.debug("Created alert: {}", TextFormat.shortDebugString(response));
      return response;
    } catch (StatusRuntimeException e) {
      var status = e.getStatus();
      log.error("Unable to create alert: code={}, description={}",
          status.getCode(), status.getDescription(), e);
      throw new AlertClientException("Failed to send create alert", e);
    }
  }

  public BatchCreateAlertMatchesResponse createMatches(BatchCreateAlertMatchesRequest request) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    if (log.isTraceEnabled()) {
      log.trace("Requesting adding matches to alert: deadline={}, request={}", deadline, request);
    }

    try {
      var response = stub.batchCreateAlertMatches(request);
      log.debug("Created matches: {}", TextFormat.shortDebugString(response));
      return response;
    } catch (StatusRuntimeException e) {
      var status = e.getStatus();
      log.error("Unable to create matches: code={}, description={}",
          status.getCode(), status.getDescription(), e);
      throw new AlertClientException("Failed to create matches", e);
    }
  }

  private static class AlertClientException extends RuntimeException {

    private static final long serialVersionUID = 2056703374022172581L;

    AlertClientException(String message, Exception e) {
      super(message, e);
    }
  }
}
