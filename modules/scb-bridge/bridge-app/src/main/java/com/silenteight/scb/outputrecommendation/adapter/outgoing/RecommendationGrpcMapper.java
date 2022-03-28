package com.silenteight.scb.outputrecommendation.adapter.outgoing;

import lombok.experimental.UtilityClass;

import com.silenteight.recommendation.api.library.v1.*;
import com.silenteight.scb.outputrecommendation.domain.model.BatchStatistics;
import com.silenteight.scb.outputrecommendation.domain.model.BatchStatistics.RecommendationsStatistics;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.*;

import java.util.HashMap;
import java.util.List;

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
        .recommendedAction(RecommendedAction.valueOf(recommendation.getRecommendedAction()))
        .recommendedComment(recommendation.getRecommendationComment())
        .policyId(recommendation.getPolicyId())
        .recommendedAt(recommendation.getRecommendedAt())
        .build();
  }

  private Alert toAlert(AlertOut alert) {
    return Alert.builder()
        .id(alert.getId())
        .status(AlertStatus.valueOf(alert.getStatus().name()))
        .metadata(alert.getMetadata())
        .errorMessage(alert.getErrorMessage())
        .build();
  }

  private Match toMatch(MatchOut match) {
    return Match.builder()
        .id(match.getId())
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
}
