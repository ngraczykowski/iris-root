package com.silenteight.scb.feeding.infrastructure.grpc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputServiceClient;
import com.silenteight.universaldatasource.api.library.agentinput.v1.BatchCreateAgentInputsIn;
import com.silenteight.universaldatasource.api.library.agentinput.v1.BatchCreateAgentInputsOut;

@Slf4j
class AgentInputServiceClientMock implements AgentInputServiceClient {

  @Override
  public <T extends Feature> BatchCreateAgentInputsOut createBatchCreateAgentInputs(
      BatchCreateAgentInputsIn<T> request) {
    return BatchCreateAgentInputsOut.builder().build();
  }
}
