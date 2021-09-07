package com.silenteight.payments.bridge.app;

import com.silenteight.payments.bridge.datasource.feature.port.incoming.AddMatchFeaturesRequest;
import com.silenteight.payments.bridge.datasource.feature.port.incoming.AddMatchFeaturesRequest.FeatureAgentInput;
import com.silenteight.payments.bridge.datasource.feature.port.incoming.AddMatchFeaturesRequest.Match;

import com.google.protobuf.Message;

import java.util.Map;
import java.util.stream.Collectors;

class IntegrationFixture {

  static AddMatchFeaturesRequest createAddMatchFeatureRequests(
      Map<String, Map<String, Message>> mapOfFeatureRequests) {
    var matches = mapOfFeatureRequests.entrySet()
        .stream()
        .map(m -> createMatch(m.getKey(), m.getValue()))
        .collect(Collectors.toList());
    return new AddMatchFeaturesRequest(matches);
  }

  private static Match createMatch(String matchName, Map<String, Message> inputs) {
    var featureInputs = inputs.entrySet()
        .stream()
        .map(m -> createFeatureInput(m.getKey(), m.getValue()))
        .collect(Collectors.toList());
    return new Match(matchName, featureInputs);
  }

  private static FeatureAgentInput createFeatureInput(String featureName, Message agentInput) {
    return new FeatureAgentInput(featureName, agentInput);
  }

}
