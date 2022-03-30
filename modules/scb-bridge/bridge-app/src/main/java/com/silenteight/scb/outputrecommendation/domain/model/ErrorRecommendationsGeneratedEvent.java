package com.silenteight.scb.outputrecommendation.domain.model;

import lombok.Builder;

public record ErrorRecommendationsGeneratedEvent(
    String batchId,
    String errorDescription,
    BatchMetadata batchMetadata,
    BatchStatistics statistics
) {

  @Builder
  public ErrorRecommendationsGeneratedEvent {}
}
