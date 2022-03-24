package com.silenteight.scb.outputrecommendation.domain;

import lombok.Builder;

import java.util.List;

public record PrepareRecommendationResponseCommand(
    String batchId,
    String analysisName,
    List<String> alertNames,
    String batchMetadata
) {

  @Builder
  public PrepareRecommendationResponseCommand {}
}
