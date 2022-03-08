package com.silenteight.payments.bridge.svb.learning.job;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.payments.bridge.datasource.agent.port.CreateAgentInputsClient;

@Slf4j
public class CreateAgentInputsClientMock implements CreateAgentInputsClient {

  private int counter;

  @Override
  public void createAgentInputs(BatchCreateAgentInputsRequest batchCreateAgentInputsRequest) {
    this.counter++;
  }

  public int callTimes() {
    return this.counter;
  }
}
