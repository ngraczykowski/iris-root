package com.silenteight.serp.governance.agent.domain.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class AgentDto {

  @NonNull
  String id;
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