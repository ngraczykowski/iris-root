package com.silenteight.payments.bridge.ae.alertregistration.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.payments.bridge.ae.alertregistration.port.AlertClientPort;

import com.google.protobuf.MessageOrBuilder;
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

  @Override
  public Alert createAlert(CreateAlertRequest request) {
    return new CreateAlertRequestTemplate().execute(request);
  }

  @Override
  public BatchCreateAlertsResponse batchCreateAlerts(BatchCreateAlertsRequest request) {
    return new BatchCreateAlertsRequestTemplate().execute(request);
  }

  @Override
  public BatchCreateMatchesResponse batchCreateMatches(BatchCreateMatchesRequest request) {
    return new BatchCreateMatchesRequestTemplate().execute(request);
  }

  @Override
  public BatchCreateAlertMatchesResponse createMatches(BatchCreateAlertMatchesRequest request) {
    return new CreateMatchesTemplate().execute(request);
  }

  private static class AlertClientException extends RuntimeException {

    private static final long serialVersionUID = 2056703374022172581L;

    AlertClientException(String message, Exception e) {
      super(message, e);
    }
  }

  private abstract class RequestTemplate<T extends MessageOrBuilder, R extends MessageOrBuilder> {

    R execute(T request) {
      var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);
      if (log.isTraceEnabled()) {
        log.trace("Requesting {}: deadline={}, request={}", operationType(), deadline, request);
      }

      try {
        var response = invoke(request, deadline);
        log.debug("Response: {}", TextFormat.shortDebugString(response));
        return response;
      } catch (StatusRuntimeException e) {
        var status = e.getStatus();
        log.error("Failed invoking {}: code={}, description={}",
            operationType(), status.getCode(), status.getDescription(), e);
        throw new AlertClientException("Failed to " + operationType(), e);
      }
    }

    abstract String operationType();

    abstract R invoke(T request, Deadline deadline);
  }

  private class BatchCreateMatchesRequestTemplate extends
      RequestTemplate<BatchCreateMatchesRequest, BatchCreateMatchesResponse> {

    @Override
    String operationType() {
      return "batch create matches";
    }

    @Override
    BatchCreateMatchesResponse invoke(BatchCreateMatchesRequest request, Deadline deadline) {
      return stub.withDeadline(deadline).batchCreateMatches(request);
    }
  }

  private class BatchCreateAlertsRequestTemplate extends
      RequestTemplate<BatchCreateAlertsRequest, BatchCreateAlertsResponse> {

    @Override
    String operationType() {
      return "batch create alerts";
    }

    @Override
    BatchCreateAlertsResponse invoke(BatchCreateAlertsRequest request, Deadline deadline) {
      return stub.withDeadline(deadline).batchCreateAlerts(request);
    }
  }

  private class CreateAlertRequestTemplate extends
      RequestTemplate<CreateAlertRequest, Alert> {

    @Override
    String operationType() {
      return "create request";
    }

    @Override
    Alert invoke(CreateAlertRequest request, Deadline deadline) {
      return stub.withDeadline(deadline).createAlert(request);
    }
  }

  private class CreateMatchesTemplate
      extends RequestTemplate<BatchCreateAlertMatchesRequest, BatchCreateAlertMatchesResponse> {

    @Override
    String operationType() {
      return "created matches";
    }

    @Override
    BatchCreateAlertMatchesResponse invoke(
        BatchCreateAlertMatchesRequest request, Deadline deadline) {
      return stub.withDeadline(deadline).batchCreateAlertMatches(request);
    }
  }

}
