/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.domain.model;

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
