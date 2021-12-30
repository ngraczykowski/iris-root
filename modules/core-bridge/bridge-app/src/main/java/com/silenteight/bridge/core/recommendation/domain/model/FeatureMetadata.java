package com.silenteight.bridge.core.recommendation.domain.model;

import lombok.Builder;

import java.util.Map;

public record FeatureMetadata(String agentConfig,
                              String solution,
                              Map<String, String> reason) {

  @Builder
  public FeatureMetadata {}
}
