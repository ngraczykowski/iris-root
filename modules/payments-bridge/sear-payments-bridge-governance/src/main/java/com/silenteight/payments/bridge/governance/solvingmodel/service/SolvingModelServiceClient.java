package com.silenteight.payments.bridge.governance.solvingmodel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc.SolvingModelServiceBlockingStub;
import com.silenteight.payments.bridge.governance.solvingmodel.model.GovernanceClientException;
import com.silenteight.sep.base.aspects.metrics.Timed;

import com.google.protobuf.Empty;
import com.google.protobuf.TextFormat;
import io.grpc.Deadline;
import io.grpc.StatusRuntimeException;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
class SolvingModelServiceClient {

  private final SolvingModelServiceBlockingStub stub;

  private final Duration timeout;

  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  SolvingModel getCurrentModel() {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);
    var request = Empty.newBuilder().build();

    if (log.isTraceEnabled()) {
      log.trace("Requesting solving model: deadline={}, request={}", deadline, request);
    }

    try {
      var result = stub.withDeadline(deadline).getDefaultSolvingModel(request);
      log.info("Got default solving model: {}", TextFormat.shortDebugString(result));
      return result;
    } catch (StatusRuntimeException e) {
      var status = e.getStatus();
      log.error("Unable to get the default model from Governance: code={}, description={}",
          status.getCode(), status.getDescription(), e);
      throw new GovernanceClientException("Failed to get the default model", e);
    }
  }
}
