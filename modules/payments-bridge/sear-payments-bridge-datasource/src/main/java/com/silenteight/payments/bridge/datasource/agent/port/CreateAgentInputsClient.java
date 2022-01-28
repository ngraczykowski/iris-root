package com.silenteight.payments.bridge.datasource.agent.port;

import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;

public interface CreateAgentInputsClient {

  void createAgentInputs(BatchCreateAgentInputsRequest batchCreateAgentInputsRequest);
}
