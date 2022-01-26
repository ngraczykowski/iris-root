package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.recommendation.domain.RecommendationFacade;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsStatistics;
import com.silenteight.bridge.core.registration.domain.model.AlertStatus;
import com.silenteight.bridge.core.registration.domain.model.BatchStatistics;
import com.silenteight.bridge.core.registration.domain.model.BatchStatistics.RecommendationsStats;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class BatchStatisticsService {

  private final AlertRepository alertRepository;
  private final RecommendationFacade recommendationFacade;

  BatchStatistics createBatchCompletedStatistics(String batchId, String analysisName) {
    var alertStatusStatistics = alertRepository.countAlertsByStatusForBatchId(batchId);
    var recommendedAlertsCount =
        alertStatusStatistics.getAlertCountByStatus(AlertStatus.RECOMMENDED);
    var errorAlertsCount = alertStatusStatistics.getAlertCountByStatus(AlertStatus.ERROR);

    return BatchStatistics.builder()
        .recommendedAlertsCount(recommendedAlertsCount)
        .totalProcessedCount(recommendedAlertsCount)
        .totalUnableToProcessCount(errorAlertsCount)
        .recommendationsStats(mapToRecommendationsStats(
            recommendationFacade.getRecommendationsStatistics(analysisName),
            errorAlertsCount))
        .build();
  }

  BatchStatistics createBatchErrorStatistics() {
    return BatchStatistics.builder()
        .totalProcessedCount(0)
        .recommendedAlertsCount(0)
        .totalUnableToProcessCount(0)
        .recommendationsStats(RecommendationsStats.builder()
            .truePositiveCount(0)
            .falsePositiveCount(0)
            .manualInvestigationCount(0)
            .errorCount(0)
            .build())
        .build();
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
