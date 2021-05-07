package com.silenteight.serp.governance.agent;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class AgentDto {

  @NonNull
  String name;
  @NonNull
  List<String> features;
  @NonNull
  List<String> solutions;

  public boolean canHandleFeature(String featureName) {
    return features.contains(featureName);
  }
}
