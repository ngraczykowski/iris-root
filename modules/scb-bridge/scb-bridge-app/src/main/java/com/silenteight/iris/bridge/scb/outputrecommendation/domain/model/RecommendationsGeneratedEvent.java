/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.domain.model;

import lombok.Builder;

import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations.Recommendation;

import java.util.List;

public record RecommendationsGeneratedEvent(
    String batchId,
    String analysisName,
    BatchMetadata batchMetadata,
    BatchStatistics statistics,
    List<Recommendation> recommendations
) {

  @Builder(toBuilder = true)
  public RecommendationsGeneratedEvent {}
}
