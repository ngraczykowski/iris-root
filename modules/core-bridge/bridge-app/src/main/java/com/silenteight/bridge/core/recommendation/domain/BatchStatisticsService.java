package com.silenteight.bridge.core.recommendation.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.recommendation.domain.model.BatchStatistics;
import com.silenteight.bridge.core.recommendation.domain.model.BatchStatistics.RecommendationsStats;
import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertStatus;
import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertWithMatchesDto;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsStatistics;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class BatchStatisticsService {

  private final RecommendationsStatisticsService statisticsService;

  BatchStatistics createBatchStatistics(
      List<AlertWithMatchesDto> alerts, List<RecommendationWithMetadata> recommendations) {

    var statusCountMap = groupByStatusCount(alerts);
    var recommendedAlertsCount = getRecommendedAlertsCount(statusCountMap);
    var errorAlertsCount = getErrorAlertsCount(statusCountMap);
    var recommendationsStatistics =
        statisticsService.createRecommendationsStatistics(recommendations);

    return BatchStatistics.builder()
        .recommendedAlertsCount(recommendedAlertsCount)
        .totalProcessedCount(recommendedAlertsCount)
        .totalUnableToProcessCount(errorAlertsCount)
        .recommendationsStats(
            mapToRecommendationsStats(recommendationsStatistics, errorAlertsCount))
        .build();
  }

  private Map<AlertStatus, Long> groupByStatusCount(List<AlertWithMatchesDto> alerts) {
    return alerts.stream()
        .collect(Collectors.groupingBy(AlertWithMatchesDto::status, Collectors.counting()));
  }

  private int getRecommendedAlertsCount(Map<AlertStatus, Long> statusCountMap) {
    return getAlertCountByStatus(statusCountMap, AlertStatus.RECOMMENDED)
        + getAlertCountByStatus(statusCountMap, AlertStatus.DELIVERED);
  }

  private int getErrorAlertsCount(Map<AlertStatus, Long> statusCountMap) {
    return getAlertCountByStatus(statusCountMap, AlertStatus.ERROR);
  }

  private int getAlertCountByStatus(Map<AlertStatus, Long> statusCountMap, AlertStatus status) {
    return statusCountMap.getOrDefault(status, 0L).intValue();
  }

  private RecommendationsStats mapToRecommendationsStats(
      RecommendationsStatistics recommendationsStatistics, Integer errorCount) {
    return RecommendationsStats.builder()
        .truePositiveCount(recommendationsStatistics.truePositiveCount())
        .falsePositiveCount(recommendationsStatistics.falsePositiveCount())
        .manualInvestigationCount(recommendationsStatistics.manualInvestigationCount())
        .errorCount(errorCount)
        .build();
  }
}
