package com.silenteight.payments.bridge.datasource.feature.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
public class MatchFeatureOutput {

  String agentInputType;

  List<MatchInput> matchInputs;

  @Value
  @Builder
  public static class MatchInput {

    String match;

    List<AgentInput> agentInputs;
  }

  @Value
  public static class AgentInput {

    String feature;

    String agentInputJson;
  }
}
