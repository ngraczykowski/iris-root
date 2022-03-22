package com.silenteight.agent.facade;

import java.util.List;

import static java.util.stream.Collectors.joining;

public interface AgentInput {

  String getMatch();

  List<? extends AgentFeatureInput> getFeatureInputs();

  default String getFeatureInputsAsString() {
    return getFeatureInputs().stream()
        .map(AgentFeatureInput::getFeature)
        .collect(joining(","));
  }
}
