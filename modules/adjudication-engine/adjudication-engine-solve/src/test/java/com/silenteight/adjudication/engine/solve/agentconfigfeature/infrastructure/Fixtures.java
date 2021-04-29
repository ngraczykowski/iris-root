package com.silenteight.adjudication.engine.solve.agentconfigfeature.infrastructure;

import lombok.NoArgsConstructor;

import com.silenteight.adjudication.engine.solve.agentconfigfeature.dto.AgentConfigFeatureName;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class Fixtures {

  protected static final String NAME_AGENT_CONFIG = "agents/name/versions/3.3.0/config/1";
  protected static final String DATE_AGENT_CONFIG = "agents/date/versions/2.1.0/config/1";

  static AgentConfigFeatureName nameAgentFeature(String featureId) {
    return new AgentConfigFeatureName(NAME_AGENT_CONFIG, "features/" + featureId);
  }

  static AgentConfigFeatureName dateAgentFeature(String featureId) {
    return new AgentConfigFeatureName(DATE_AGENT_CONFIG, "features/" + featureId);
  }
}
