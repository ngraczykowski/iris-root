package com.silenteight.bridge.core.recommendation.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsStatistics;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendedActionStatistics;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository;

import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
class RecommendationsStatisticsService {

  private final RecommendationRepository recommendationRepository;

  RecommendationsStatistics createRecommendationsStatistics(String analysisName) {
    var recommendedActionStatistics =
        recommendationRepository.countRecommendationsByActionForAnalysisName(analysisName);

    return RecommendationsStatistics.builder()
        .truePositiveCount(recommendedActionStatistics.getRecommendedActionCount(
            RecommendedAction.ACTION_POTENTIAL_TRUE_POSITIVE.name()))
        .falsePositiveCount(recommendedActionStatistics.getRecommendedActionCount(
            RecommendedAction.ACTION_FALSE_POSITIVE.name()))
        .manualInvestigationCount(getManualInvestigationCount(recommendedActionStatistics))
        .build();
  }

  private Integer getManualInvestigationCount(RecommendedActionStatistics statistics) {
    return Stream.of(
            RecommendedAction.ACTION_INVESTIGATE,
            RecommendedAction.ACTION_INVESTIGATE_HINTED_FALSE_POSITIVE,
            RecommendedAction.ACTION_INVESTIGATE_PARTIALLY_FALSE_POSITIVE,
            RecommendedAction.ACTION_INVESTIGATE_HINTED_TRUE_POSITIVE
        )
        .mapToInt(action -> statistics.getRecommendedActionCount(action.name()))
        .sum();
  }

  enum RecommendedAction {
    ACTION_INVESTIGATE,
    ACTION_INVESTIGATE_HINTED_FALSE_POSITIVE,
    ACTION_INVESTIGATE_PARTIALLY_FALSE_POSITIVE,
    ACTION_INVESTIGATE_HINTED_TRUE_POSITIVE,
    ACTION_FALSE_POSITIVE,
    ACTION_POTENTIAL_TRUE_POSITIVE
  }
}
