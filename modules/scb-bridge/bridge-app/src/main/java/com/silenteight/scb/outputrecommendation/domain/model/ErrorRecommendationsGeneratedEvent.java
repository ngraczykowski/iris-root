package com.silenteight.scb.outputrecommendation.domain.model;

import lombok.Builder;

public record ErrorRecommendationsGeneratedEvent(
    String batchId,
    String errorDescription,
    String batchMetadata,
    BatchStatistics statistics
) {

  @Builder
  public ErrorRecommendationsGeneratedEvent {}
}
