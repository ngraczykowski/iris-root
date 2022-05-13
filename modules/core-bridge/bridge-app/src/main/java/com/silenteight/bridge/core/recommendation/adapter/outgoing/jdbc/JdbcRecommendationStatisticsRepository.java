package com.silenteight.bridge.core.recommendation.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
class JdbcRecommendationStatisticsRepository implements RecommendationStatisticsRepository {

  private final CrudRecommendationStatisticsRepository statisticsRepository;

  @Override
  public Map<String, Long> getRecommendationStatistics(String analysisName) {
    return statisticsRepository.getRecommendationStatistics(analysisName)
        .stream()
        .collect(Collectors.toMap(RecommendationStatisticProjection::recommendedAction,
            RecommendationStatisticProjection::count, (v1, v2) -> v1));
  }

  @Override
  public Map<String, Long> getRecommendationStatistics(
      String analysisName, List<String> alertsNames) {
    return statisticsRepository.getRecommendationStatistics(analysisName, alertsNames)
        .stream()
        .collect(Collectors.toMap(RecommendationStatisticProjection::recommendedAction,
            RecommendationStatisticProjection::count, (v1, v2) -> v1));
  }
}
