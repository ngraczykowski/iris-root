package com.silenteight.bridge.core.recommendation.domain.model;

import lombok.Builder;

import java.util.Map;

public record MatchMetadata(
    String match,
    String solution,
    Map<String, String> reason,
    Map<String, String> categories,
    Map<String, FeatureMetadata> features
) {

  @Builder
  public MatchMetadata {}
}
