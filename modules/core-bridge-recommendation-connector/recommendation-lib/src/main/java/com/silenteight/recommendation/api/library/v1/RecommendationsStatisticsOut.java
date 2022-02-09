package com.silenteight.recommendation.api.library.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.proto.recommendation.api.v1.RecommendationsStatistics;

@Value
@Builder
public class RecommendationsStatisticsOut {

  Integer truePositiveCount;
  Integer falsePositiveCount;
  Integer manualInvestigationCount;
  Integer errorCount;

  static RecommendationsStatisticsOut createFrom(
      RecommendationsStatistics recommendationsStatistics) {
    return RecommendationsStatisticsOut.builder()
        .truePositiveCount(recommendationsStatistics.getTruePositiveCount())
        .falsePositiveCount(recommendationsStatistics.getFalsePositiveCount())
        .manualInvestigationCount(recommendationsStatistics.getManualInvestigationCount())
        .errorCount(recommendationsStatistics.getErrorCount())
        .build();
  }
}
