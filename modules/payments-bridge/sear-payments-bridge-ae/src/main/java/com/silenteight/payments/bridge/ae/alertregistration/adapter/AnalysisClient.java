package com.silenteight.payments.bridge.ae.alertregistration.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;
import com.silenteight.adjudication.api.v1.BatchAddAlertsRequest;
import com.silenteight.adjudication.api.v1.BatchAddAlertsResponse;
import com.silenteight.adjudication.api.v1.CreateAnalysisRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.AnalysisClientPort;

import io.grpc.Deadline;
import io.grpc.StatusRuntimeException;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
class AnalysisClient implements AnalysisClientPort {

  private final AnalysisServiceBlockingStub stub;

  private final Duration timeout;

  public Analysis createAnalysis(CreateAnalysisRequest request) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    if (log.isTraceEnabled()) {
      log.trace("Requesting creating analysis: deadline={}, request={}", deadline, request);
    }

    try {
      var result = stub.withDeadline(deadline).createAnalysis(request);
      log.info("Created analysis with result = {}", result);
      return result;
    } catch (StatusRuntimeException status) {
      log.warn("Failed to send create analysis", status);
      throw new AnalysisClientException("Failed to send create analysis", status);
    }
  }

  public BatchAddAlertsResponse addAlertToAnalysis(BatchAddAlertsRequest request) {

    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    if (log.isTraceEnabled()) {
      log.trace("Requesting adding alert to analysis: deadline={}, request={}", deadline, request);
    }

    try {
      var result = stub.withDeadline(deadline).batchAddAlerts(request);
      log.info("Created analysis with result = {}", result);
      return result;
    } catch (StatusRuntimeException status) {
      log.warn("Failed to send add alerts to analysis", status);
      throw new AnalysisClientException("Failed to send add alerts to analysis", status);
    }
  }

  private static class AnalysisClientException extends RuntimeException {

    private static final long serialVersionUID = 5869310708376584272L;

    AnalysisClientException(String message, Exception e) {
      super(message, e);
    }
  }
}
