package com.silenteight.payments.bridge.svb.learning.features.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.svb.learning.features.port.incoming.CreateFeaturesUseCase;
import com.silenteight.payments.bridge.svb.learning.features.port.outgoing.CreateAgentInputsClient;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
class CreateFeaturesService implements CreateFeaturesUseCase {

  private final CreateAgentInputsClient createAgentInputsClient;
  private final List<FeatureExtractor> featureExtractors;

  public List<AgentInput> createMatchFeatures(LearningAlert learningAlert) {
    var agentInputs = new ArrayList<AgentInput>();

    learningAlert.getMatches().forEach(match -> {
      var features = new ArrayList<FeatureInput>();
      featureExtractors.forEach(fe -> features.add(fe.extract(match)));
      agentInputs.add(AgentInput
          .newBuilder()
          .setMatch(match.getMatchName())
          .addAllFeatureInputs(features)
          .build());
    });

    createAgentInputsClient.createAgentInputs(
        BatchCreateAgentInputsRequest.newBuilder().addAllAgentInputs(agentInputs).build());

    return agentInputs;
  }

  @Override
  public void createMatchFeatures(List<LearningAlert> learningAlerts) {
    var inputs = learningAlerts.stream()
        .flatMap(this::createAgentInputs).collect(Collectors.toList());
    createAgentInputsClient.createAgentInputs(
        BatchCreateAgentInputsRequest.newBuilder().addAllAgentInputs(inputs).build());
  }

  private Stream<AgentInput> createAgentInputs(LearningAlert learningAlert) {
    return learningAlert.getMatches().stream()
        .map(match ->
          AgentInput
              .newBuilder()
              .setMatch(match.getMatchName())
              .addAllFeatureInputs(toFeatureInput(match))
              .build()
        );
  }

  private List<FeatureInput> toFeatureInput(LearningMatch learningMatch) {
    return featureExtractors.stream().map(fe -> fe.extract(learningMatch))
        .collect(Collectors.toList());
  }


}
