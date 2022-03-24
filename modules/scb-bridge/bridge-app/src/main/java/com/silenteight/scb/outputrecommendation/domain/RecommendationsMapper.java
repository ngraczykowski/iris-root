package com.silenteight.scb.outputrecommendation.domain;

import lombok.experimental.UtilityClass;

import com.silenteight.scb.outputrecommendation.domain.model.*;
import com.silenteight.scb.outputrecommendation.domain.model.BatchStatistics.RecommendationsStatistics;

import java.util.List;

@UtilityClass
class RecommendationsMapper {

  RecommendationsGeneratedEvent toBatchCompletedRecommendationsEvent(
      PrepareRecommendationResponseCommand command,
      Recommendations recommendations) {

    return RecommendationsGeneratedEvent.builder()
        .batchId(command.batchId())
        .analysisName(command.analysisName())
        .alertNames(command.alertNames())
        .batchMetadata(command.batchMetadata())
        .statistics(recommendations.statistics())
        .recommendations(recommendations.recommendations())
        .build();
  }

  RecommendationsDeliveredEvent toRecommendationsDeliveredEvent(
      String batchId, String analysisName, List<String> alertNames) {

    return new RecommendationsDeliveredEvent(batchId, analysisName, alertNames);
  }

  ErrorRecommendationsGeneratedEvent toBatchErrorRecommendationsEvent(
      PrepareErrorRecommendationResponseCommand command) {

    return ErrorRecommendationsGeneratedEvent.builder()
        .batchId(command.batchId())
        .errorDescription(command.errorDescription())
        .batchMetadata(command.batchMetadata())
        .statistics(createEmptyStatistics())
        .build();
  }

  BatchStatistics createEmptyStatistics() {
    return BatchStatistics.builder()
        .totalProcessedCount(0)
        .totalUnableToProcessCount(0)
        .recommendedAlertsCount(0)
        .recommendationsStatistics(RecommendationsStatistics.builder()
            .truePositiveCount(0)
            .falsePositiveCount(0)
            .manualInvestigationCount(0)
            .errorCount(0)
            .build())
        .build();
  }
}
