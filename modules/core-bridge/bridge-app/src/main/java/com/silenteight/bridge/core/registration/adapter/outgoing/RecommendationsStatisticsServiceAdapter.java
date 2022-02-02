package com.silenteight.bridge.core.registration.adapter.outgoing;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.recommendation.domain.model.RecommendedActionStatistics;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository;
import com.silenteight.bridge.core.registration.domain.model.RecommendationsStatistics;
import com.silenteight.bridge.core.registration.domain.port.outgoing.RecommendationsStatisticsService;

import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
class RecommendationsStatisticsServiceAdapter implements RecommendationsStatisticsService {

  /* TODO
  Because of circular bean dependency involved with
  RecommendationFacade -> BatchStatisticsService we need to do
  https://silent8.atlassian.net/browse/ALL-600 task.
  */
  private final RecommendationRepository repository;

  public RecommendationsStatistics createRecommendationsStatistics(String analysisName) {
    var recommendedActionStatistics =
        repository.countRecommendationsByActionForAnalysisName(analysisName);

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
