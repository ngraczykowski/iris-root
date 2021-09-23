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

    return stub.withDeadline(deadline).createAnalysis(request);
  }

  public BatchAddAlertsResponse addAlertToAnalysis(BatchAddAlertsRequest request) {

    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    if (log.isTraceEnabled()) {
      log.trace("Requesting adding alert to analysis: deadline={}, request={}", deadline, request);
    }

    return stub.withDeadline(deadline).batchAddAlerts(request);
  }
}
