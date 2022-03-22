package com.silenteight.payments.bridge.datasource.agent;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;

import java.util.List;

public interface FeatureInputRepository {

  void save(List<AgentInput> agentInputs);
}
