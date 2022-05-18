package com.silenteight.payments.bridge.datasource.agent.infrastructure;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.payments.bridge.datasource.agent.FeatureInputRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class RemoteDatasourceFeatureInputRepository implements FeatureInputRepository {

  private final CreateAgentInputsClient createAgentInputsClient;

  @Override
  public void save(List<AgentInput> agentInputs) {
    var batchRequest = createBatchRequest(agentInputs);
    createAgentInputsClient.createAgentInputs(batchRequest);
  }

  private static BatchCreateAgentInputsRequest createBatchRequest(List<AgentInput> agentInputs) {
    return BatchCreateAgentInputsRequest.newBuilder()
        .addAllAgentInputs(agentInputs)
        .build();
  }
}
