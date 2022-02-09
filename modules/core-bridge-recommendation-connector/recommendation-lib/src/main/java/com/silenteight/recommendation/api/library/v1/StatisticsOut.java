package com.silenteight.recommendation.api.library.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.proto.recommendation.api.v1.Statistics;

@Value
@Builder
public class StatisticsOut {

  Integer totalProcessedCount;
  Integer totalUnableToProcessCount;
  Integer recommendedAlertsCount;
  RecommendationsStatisticsOut recommendationsStatistics;

  static StatisticsOut createFrom(Statistics statistics) {
    return StatisticsOut
        .builder()
        .totalProcessedCount(statistics.getTotalProcessedCount())
        .totalUnableToProcessCount(statistics.getTotalUnableToProcessCount())
        .recommendedAlertsCount(statistics.getRecommendedAlertsCount())
        .recommendationsStatistics(
            RecommendationsStatisticsOut.createFrom(statistics.getRecommendationsStatistics()))
        .build();
  }
}
