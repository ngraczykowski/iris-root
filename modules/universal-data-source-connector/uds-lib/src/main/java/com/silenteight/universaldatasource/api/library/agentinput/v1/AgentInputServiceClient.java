package com.silenteight.universaldatasource.api.library.agentinput.v1;

import com.silenteight.universaldatasource.api.library.Feature;

public interface AgentInputServiceClient {

  <T extends Feature> BatchCreateAgentInputsOut createBatchCreateAgentInputs(
      BatchCreateAgentInputsIn<T> request);
}
