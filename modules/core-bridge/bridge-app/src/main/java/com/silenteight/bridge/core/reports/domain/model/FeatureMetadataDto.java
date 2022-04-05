package com.silenteight.bridge.core.reports.domain.model;

import lombok.Builder;

import java.util.Map;

public record FeatureMetadataDto(String agentConfig,
                                 String solution,
                                 Map<String, String> reason) {

  @Builder
  public FeatureMetadataDto {}
}
