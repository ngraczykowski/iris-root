package com.silenteight.scb.outputrecommendation.domain.model;

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
