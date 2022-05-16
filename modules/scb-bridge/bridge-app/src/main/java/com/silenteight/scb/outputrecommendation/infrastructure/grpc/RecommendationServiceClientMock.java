package com.silenteight.scb.outputrecommendation.infrastructure.grpc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.recommendation.api.library.v1.*;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class RecommendationServiceClientMock implements RecommendationServiceClient {

  private final ConcurrentMap<String, List<RecommendationOut>> data = new ConcurrentHashMap<>();

  @Override
  public RecommendationsOut getRecommendations(RecommendationsIn request) {
    log.info("MOCK: Get recommendations called for analysis id: {}", request.getAnalysisName());
    var list = data.remove(request.getAnalysisName());
    if (list == null) {
      throw new IllegalStateException(
          "No recommendations for analysis: " + request.getAnalysisName());
    }
    return RecommendationsOut.builder()
        .recommendations(list)
        .statistics(StatisticsOut.builder()
            .totalProcessedCount(5)
            .totalUnableToProcessCount(0)
            .recommendedAlertsCount(0)
            .recommendationsStatistics(RecommendationsStatisticsOut.builder()
                .truePositiveCount(5)
                .falsePositiveCount(0)
                .manualInvestigationCount(0)
                .errorCount(0)
                .build())
            .build())
        .build();
  }

  public void add(String analysisName, List<RecommendationOut> recommendations) {
    data.put(analysisName, recommendations);
  }

}
