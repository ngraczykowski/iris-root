package com.silenteight.universaldatasource.app;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.datasource.agentinput.api.v1.FeatureInput;

import com.google.protobuf.Any;
import com.google.protobuf.Message;

import java.util.Map;
import java.util.stream.Collectors;

class IntegrationFixture {

  static BatchCreateAgentInputsRequest getBatchCreateAgentInputsRequest(
      Map<String, Map<String, Message>> mapOfFeatureRequests) {

    var agentInputs = mapOfFeatureRequests.entrySet()
        .stream()
        .map(m -> createAgentInput(m.getKey(), m.getValue()))
        .collect(Collectors.toList());

    return BatchCreateAgentInputsRequest.newBuilder()
        .addAllAgentInputs(agentInputs)
        .build();
  }

  private static AgentInput createAgentInput(String match, Map<String, Message> value) {

    var featureInputs = value.entrySet().stream()
        .map(v -> FeatureInput
            .newBuilder()
            .setFeature(v.getKey())
            .setAgentFeatureInput(Any.pack(v.getValue()))
            .build())
        .collect(Collectors.toList());

    return AgentInput.newBuilder()
        .setMatch(match)
        .addAllFeatureInputs(featureInputs)
        .build();
  }
}
