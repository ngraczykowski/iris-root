package com.silenteight.universaldatasource.app.feature.model;

import lombok.Value;

@Value
public class MatchFeatureInput {

  String match;

  String feature;

  String agentInputType;

  String agentInput;
}
