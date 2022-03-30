package com.silenteight.scb.outputrecommendation.domain;

import lombok.Builder;

public record PrepareRecommendationResponseCommand(
    String batchId,
    String analysisName,
    String batchMetadata
) {

  @Builder
  public PrepareRecommendationResponseCommand {}
}
