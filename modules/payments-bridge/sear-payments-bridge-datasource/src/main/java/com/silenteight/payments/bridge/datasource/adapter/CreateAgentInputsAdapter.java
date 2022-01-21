package com.silenteight.payments.bridge.datasource.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.payments.bridge.datasource.port.CreateAgentInputsClient;

import io.grpc.Deadline;
import io.grpc.StatusRuntimeException;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
class CreateAgentInputsAdapter implements CreateAgentInputsClient {

  private final AgentInputServiceBlockingStub blockingStub;

  private final Duration timeout;

  public void createAgentInputs(BatchCreateAgentInputsRequest batchCreateAgentInputsRequest) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    if (log.isTraceEnabled()) {
      log.trace("Sending create agent inputs request");
    }

    try {
      var response = blockingStub
          .withDeadline(deadline)
          .batchCreateAgentInputs(batchCreateAgentInputsRequest);
      log.trace("Created agent inputs for = {}", response);
    } catch (StatusRuntimeException status) {
      log.warn("Request to the datasource service failed", status);
    }
  }
}
