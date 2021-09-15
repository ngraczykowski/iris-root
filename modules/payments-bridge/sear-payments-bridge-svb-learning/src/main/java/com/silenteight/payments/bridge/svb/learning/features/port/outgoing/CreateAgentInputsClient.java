package com.silenteight.payments.bridge.svb.learning.features.port.outgoing;

import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;

public interface CreateAgentInputsClient {

  void createAgentInputs(BatchCreateAgentInputsRequest batchCreateAgentInputsRequest);
}
