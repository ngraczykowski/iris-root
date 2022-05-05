package com.silenteight.bridge.core.reports.domain.model;

import lombok.Builder;

import java.util.Map;

public record MatchMetadataDto(
    String match,
    String solution,
    Map<String, String> reason,
    Map<String, String> categories,
    Map<String, FeatureMetadataDto> features,
    String matchComment
) {

  @Builder
  public MatchMetadataDto {}
}
