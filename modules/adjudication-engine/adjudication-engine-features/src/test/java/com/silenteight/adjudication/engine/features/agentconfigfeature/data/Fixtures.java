package com.silenteight.adjudication.engine.features.agentconfigfeature.data;

import lombok.NoArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis.Feature;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class Fixtures {

  protected static final String NAME_AGENT_CONFIG = "agents/name/versions/3.3.0/config/1";
  protected static final String DATE_AGENT_CONFIG = "agents/date/versions/2.1.0/config/1";

  static Feature nameAgentFeature(String featureId) {
    return Feature.newBuilder()
        .setAgentConfig(NAME_AGENT_CONFIG)
        .setFeature("features/" + featureId)
        .build();
  }

  static Feature dateAgentFeature(String featureId) {
    return Feature.newBuilder()
        .setAgentConfig(DATE_AGENT_CONFIG)
        .setFeature("features/" + featureId)
        .build();
  }
}
