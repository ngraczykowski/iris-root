/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.domain.model;

import lombok.Builder;

public record BatchStatistics(Integer totalProcessedCount,
                              Integer totalUnableToProcessCount,
                              Integer recommendedAlertsCount,
                              RecommendationsStatistics recommendationsStatistics) {

  @Builder
  public BatchStatistics {}

  public static record RecommendationsStatistics(
      Integer truePositiveCount,
      Integer falsePositiveCount,
      Integer manualInvestigationCount,
      Integer errorCount
  ) {

    @Builder
    public RecommendationsStatistics {}
  }
}
