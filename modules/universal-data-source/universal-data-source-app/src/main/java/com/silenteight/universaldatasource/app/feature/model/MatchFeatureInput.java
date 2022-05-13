package com.silenteight.universaldatasource.app.feature.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MatchFeatureInput {

  String alert;

  String match;

  String feature;

  String agentInputType;

  String agentInput;
}
