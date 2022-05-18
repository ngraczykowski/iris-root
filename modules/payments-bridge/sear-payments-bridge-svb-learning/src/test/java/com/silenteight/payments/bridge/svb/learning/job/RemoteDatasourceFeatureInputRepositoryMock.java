package com.silenteight.payments.bridge.svb.learning.job;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.payments.bridge.datasource.agent.FeatureInputRepository;

import java.util.List;

class RemoteDatasourceFeatureInputRepositoryMock implements FeatureInputRepository {

  @Override
  public void save(List<AgentInput> agentInputs) {

  }
}
