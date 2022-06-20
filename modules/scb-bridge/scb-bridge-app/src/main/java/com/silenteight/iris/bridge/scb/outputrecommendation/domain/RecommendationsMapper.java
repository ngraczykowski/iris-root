/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.domain.payload.PayloadConverter;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.*;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.BatchStatistics.RecommendationsStatistics;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RecommendationsMapper {

  private final PayloadConverter converter;

  RecommendationsGeneratedEvent toBatchCompletedRecommendationsEvent(
      PrepareRecommendationResponseCommand command,
      Recommendations recommendations) {

    return RecommendationsGeneratedEvent.builder()
        .batchId(command.batchId())
        .analysisName(command.analysisName())
        .batchMetadata(
            converter.deserializeFromJsonToObject(command.batchMetadata(), BatchMetadata.class))
        .statistics(recommendations.statistics())
        .recommendations(recommendations.recommendations())
        .build();
  }

  RecommendationsDeliveredEvent toRecommendationsDeliveredEvent(
      String batchId, String analysisName) {

    return new RecommendationsDeliveredEvent(batchId, analysisName);
  }

  ErrorRecommendationsGeneratedEvent toBatchErrorRecommendationsEvent(
      PrepareErrorRecommendationResponseCommand command) {

    return ErrorRecommendationsGeneratedEvent.builder()
        .batchId(command.batchId())
        .errorDescription(command.errorDescription())
        .batchMetadata(
            converter.deserializeFromJsonToObject(command.batchMetadata(), BatchMetadata.class))
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
