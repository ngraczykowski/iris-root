/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.adapter.outgoing;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.BatchStatistics;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.BatchStatistics.RecommendationsStatistics;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations.*;
import com.silenteight.recommendation.api.library.v1.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@UtilityClass
class RecommendationGrpcMapper {

  Recommendations toResponse(RecommendationsOut recommendationsOut) {
    var recommendations = recommendationsOut.getRecommendations().stream()
        .map(RecommendationGrpcMapper::toRecommendation)
        .toList();
    return Recommendations.builder()
        .recommendations(recommendations)
        .statistics(toBatchStatistics(recommendationsOut.getStatistics()))
        .build();
  }

  RecommendationsIn toRequest(String analysisName, List<String> alertNames) {
    return RecommendationsIn.builder()
        .analysisName(analysisName)
        .alertNames(alertNames)
        .build();
  }

  private Recommendation toRecommendation(RecommendationOut recommendation) {
    return Recommendation.builder()
        .alert(RecommendationGrpcMapper.toAlert(recommendation.getAlert()))
        .matches(
            recommendation.getMatches().stream().map(RecommendationGrpcMapper::toMatch).toList())
        .batchId(recommendation.getBatchId())
        .name(recommendation.getName())
        .recommendedAction(toRecommendedAction(recommendation.getRecommendedAction()))
        .recommendedComment(recommendation.getRecommendationComment())
        .policyId(recommendation.getPolicyId())
        .recommendedAt(recommendation.getRecommendedAt())
        .build();
  }

  private Alert toAlert(AlertOut alert) {
    return Alert.builder()
        .id(alert.getId())
        .name(alert.getName())
        .status(AlertStatus.valueOf(alert.getStatus().name()))
        .metadata(alert.getMetadata())
        .errorMessage(alert.getErrorMessage())
        .build();
  }

  private Match toMatch(MatchOut match) {
    return Match.builder()
        .id(match.getId())
        .name(match.getName())
        .recommendedAction(match.getRecommendedAction())
        .recommendedComment(match.getRecommendationComment())
        .stepId(match.getStepId())
        .fvSignature(match.getFvSignature())
        .features(new HashMap<>(match.getFeatures()))
        .build();
  }

  private BatchStatistics toBatchStatistics(StatisticsOut statistics) {
    var recommendationsStatistics = statistics.getRecommendationsStatistics();
    return BatchStatistics.builder()
        .totalProcessedCount(statistics.getTotalProcessedCount())
        .totalUnableToProcessCount(statistics.getTotalUnableToProcessCount())
        .recommendedAlertsCount(statistics.getRecommendedAlertsCount())
        .recommendationsStatistics(RecommendationsStatistics.builder()
            .truePositiveCount(recommendationsStatistics.getTruePositiveCount())
            .falsePositiveCount(recommendationsStatistics.getFalsePositiveCount())
            .manualInvestigationCount(
                recommendationsStatistics.getManualInvestigationCount())
            .errorCount(recommendationsStatistics.getErrorCount())
            .build())
        .build();
  }

  private RecommendedAction toRecommendedAction(String value) {
    try {
      return RecommendedAction.valueOf(value);
    } catch (Exception e) {
      log.error("Error was encountered while mapping {} value to RecommendedAction", value, e);
      return RecommendedAction.ACTION_INVESTIGATE;
    }
  }
}
