package com.silenteight.payments.bridge.svb.learning.job;

import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.payments.bridge.datasource.agent.port.CreateAgentInputsClient;

class CreateAgentInputsClientMock implements CreateAgentInputsClient {

  @Override
  public void createAgentInputs(BatchCreateAgentInputsRequest batchCreateAgentInputsRequest) {

  }
}
