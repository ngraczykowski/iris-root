package com.silenteight.bridge.core.recommendation.domain.model;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record BatchStatistics(Integer totalProcessedCount,
                              Integer recommendedAlertsCount,
                              Integer totalUnableToProcessCount,
                              RecommendationsStats recommendationsStats) {

  @Builder
  public BatchStatistics {}

  public static BatchStatisticsBuilder builder() {
    return new BatchStatisticsBuilder() {
      @Override
      public BatchStatistics build() {
        validateSumOfRecommendations();
        return super.build();
      }
    };
  }

  public static class BatchStatisticsBuilder {

    void validateSumOfRecommendations() {
      var sumOfRecommendations =
          recommendationsStats.truePositiveCount + recommendationsStats.falsePositiveCount
              + recommendationsStats.manualInvestigationCount;

      if (sumOfRecommendations != totalProcessedCount) {
        log.error(
            "Batch statistics creation failed. "
                + "TotalProcessedCount: {} is not equal to sum of recommendations: {}.",
            totalProcessedCount, sumOfRecommendations);
      }
    }
  }

  public static record RecommendationsStats(Integer truePositiveCount,
                                            Integer falsePositiveCount,
                                            Integer manualInvestigationCount,
                                            Integer errorCount) {

    @Builder
    public RecommendationsStats {}
  }
}
