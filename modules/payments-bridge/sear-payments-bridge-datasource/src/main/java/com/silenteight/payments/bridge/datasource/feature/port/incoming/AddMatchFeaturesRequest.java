package com.silenteight.payments.bridge.datasource.feature.port.incoming;

import lombok.Value;

import com.google.protobuf.Message;

import java.util.List;

@Value
public class AddMatchFeaturesRequest {

  List<Match> matches;

  @Value
  public static class Match {

    String matchName;

    List<FeatureAgentInput> inputs;
  }

  @Value
  public static class FeatureAgentInput {

    String featureName;

    // LocationFeatureInput, NameFeatureInput, etc.
    Message agentInput;
  }
}
