package com.silenteight.universaldatasource.app.feature.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsResponse;
import com.silenteight.datasource.agentinput.api.v1.CreatedAgentInput;
import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.sep.base.common.protocol.MessageRegistry;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureInput;
import com.silenteight.universaldatasource.app.feature.port.incoming.BatchCreateMatchFeaturesUseCase;
import com.silenteight.universaldatasource.app.feature.port.outgoing.FeatureDataAccess;
import com.silenteight.universaldatasource.app.feature.port.outgoing.FeatureMapper;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
class BatchCreateMatchFeaturesService implements BatchCreateMatchFeaturesUseCase {

  private static final int MAX_BATCH_SIZE = 4096;

  private final MessageRegistry messageRegistry;
  private final FeatureDataAccess dataAccess;
  private final Map<String, FeatureMapper> mappers;

  @Timed(value = "uds.feature.use_cases", extraTags = { "action", "batchCreateFeatures" })
  @Override
  public BatchCreateAgentInputsResponse batchCreateMatchFeatures(
      Collection<AgentInput> agentInputs) {
    var createdAgentInputs = getCreatedAgentInputs(agentInputs);

    if (log.isDebugEnabled()) {
      log.debug("Saved feature inputs: count={}", createdAgentInputs.size());
    }

    return BatchCreateAgentInputsResponse.newBuilder()
        .addAllCreatedAgentInputs(createdAgentInputs)
        .build();
  }

  private List<CreatedAgentInput> getCreatedAgentInputs(Collection<AgentInput> agentInputs) {
    List<MatchFeatureInput> matchFeatureInputs = new ArrayList<>();
    List<CreatedAgentInput> createdAgentInputs = new ArrayList<>();

    // NOTE(jgajewski): For those afraid of such a loop nesting: the input agentInputs collection
    //  will have very small number of elements.
    for (var agentInput : agentInputs) {
      for (var featureInput : agentInput.getFeatureInputsList()) {
        matchFeatureInputs.add(createFeatureInput(agentInput, featureInput));
        if (matchFeatureInputs.size() >= MAX_BATCH_SIZE) {
          createdAgentInputs.addAll(dataAccess.saveAll(matchFeatureInputs));
          matchFeatureInputs.clear();
        }
      }
    }
    createdAgentInputs.addAll(dataAccess.saveAll(matchFeatureInputs));
    return createdAgentInputs;
  }

  private MatchFeatureInput createFeatureInput(
      AgentInput agentInput, FeatureInput featureAgentInput) {
    try {
      return map(agentInput, featureAgentInput);
    } catch (InvalidProtocolBufferException e) {
      throw new MatchFeatureInputMappingException(e);
    }
  }

  private MatchFeatureInput map(AgentInput agentInput, FeatureInput featureInput) throws
      InvalidProtocolBufferException {

    var agentFeatureInput = featureInput.getAgentFeatureInput();
    var featureInputMessage = getFeatureInputClass(agentFeatureInput);
    var agentInputJson = messageRegistry.toJson(featureInputMessage);

    return new MatchFeatureInput(agentInput.getMatch(), featureInput.getFeature(),
        featureInputMessage.getClass().getSimpleName(), agentInputJson);
  }

  private Message getFeatureInputClass(Any agentFeatureInput) throws
      InvalidProtocolBufferException {

    var featureInputType = getAgentInputClassName(agentFeatureInput.getTypeUrl());

    var featureMapper = getFeatureMapper(featureInputType);
    return featureMapper.unpackAnyMessage(agentFeatureInput);
  }

  private static String getAgentInputClassName(String classTypeUrl) {
    var arrayOfClassPath = classTypeUrl.split("\\.");

    if (arrayOfClassPath.length < 1)
      throw new NoSuchElementException(
          "No class was found for agent input type, with url: " + classTypeUrl);

    return arrayOfClassPath[arrayOfClassPath.length - 1];
  }

  private FeatureMapper getFeatureMapper(String featureInputType) {
    if (!mappers.containsKey(featureInputType))
      throw new NoSuchElementException(
          "Agent name read from agent_feature_input not found: " + featureInputType);

    return mappers.get(featureInputType);
  }
}
