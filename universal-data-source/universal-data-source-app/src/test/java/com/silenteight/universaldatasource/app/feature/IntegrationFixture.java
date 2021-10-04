package com.silenteight.universaldatasource.app.feature;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.datasource.agentinput.api.v1.FeatureInput;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Struct;
import com.google.protobuf.Struct.Builder;
import com.google.protobuf.util.JsonFormat;

import java.util.Map;
import java.util.stream.Collectors;

class IntegrationFixture {

  private static final ObjectMapper MAPPER = new ObjectMapper();

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

  static Struct getIsPepReason() throws InvalidProtocolBufferException {
    ObjectNode reasonObjectNodeOne = MAPPER.createObjectNode();
    reasonObjectNodeOne.set(
        "field1", (MAPPER.convertValue("Reason number 1", JsonNode.class)));
    Builder reasonBuilder = Struct.newBuilder();
    JsonFormat.parser().merge(reasonObjectNodeOne.toString(), reasonBuilder);
    return reasonBuilder.build();
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
