package com.silenteight.bridge.core.recommendation.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsStatistics;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
class RecommendationsStatisticsService {

  private enum RecommendedAction {
    ACTION_INVESTIGATE,
    ACTION_INVESTIGATE_HINTED_FALSE_POSITIVE,
    ACTION_INVESTIGATE_PARTIALLY_FALSE_POSITIVE,
    ACTION_INVESTIGATE_HINTED_TRUE_POSITIVE,
    ACTION_FALSE_POSITIVE,
    ACTION_POTENTIAL_TRUE_POSITIVE
  }

  RecommendationsStatistics createRecommendationsStatistics(
      List<RecommendationWithMetadata> recommendations) {
    var actionCountMap = groupByActionCount(recommendations);

    return RecommendationsStatistics
        .builder()
        .truePositiveCount(
            getRecommendationCountByAction(
                actionCountMap, RecommendedAction.ACTION_POTENTIAL_TRUE_POSITIVE))
        .falsePositiveCount(
            getRecommendationCountByAction(actionCountMap, RecommendedAction.ACTION_FALSE_POSITIVE))
        .manualInvestigationCount(getManualInvestigationCount(actionCountMap))
        .build();
  }

  private Map<String, Long> groupByActionCount(List<RecommendationWithMetadata> recommendations) {
    return recommendations
        .stream()
        .collect(Collectors.groupingBy(
            RecommendationWithMetadata::recommendedAction,
            Collectors.counting()));
  }

  private int getRecommendationCountByAction(Map<String, Long> counter, RecommendedAction action) {
    return counter.getOrDefault(action.name(), 0L).intValue();
  }

  private int getManualInvestigationCount(Map<String, Long> actionCountMap) {
    return Stream
        .of(
            RecommendedAction.ACTION_INVESTIGATE,
            RecommendedAction.ACTION_INVESTIGATE_HINTED_FALSE_POSITIVE,
            RecommendedAction.ACTION_INVESTIGATE_PARTIALLY_FALSE_POSITIVE,
            RecommendedAction.ACTION_INVESTIGATE_HINTED_TRUE_POSITIVE)
        .mapToInt(action -> getRecommendationCountByAction(actionCountMap, action))
        .sum();
  }
}
