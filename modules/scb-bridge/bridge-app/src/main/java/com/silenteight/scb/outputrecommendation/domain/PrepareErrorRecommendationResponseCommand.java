package com.silenteight.scb.outputrecommendation.domain;

import lombok.Builder;

public record PrepareErrorRecommendationResponseCommand(
    String batchId,
    String errorDescription,
    String batchMetadata
) {

  @Builder
  public PrepareErrorRecommendationResponseCommand {}
}
