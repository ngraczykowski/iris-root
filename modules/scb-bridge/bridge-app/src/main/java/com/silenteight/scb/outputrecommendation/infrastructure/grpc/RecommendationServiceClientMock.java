package com.silenteight.scb.outputrecommendation.infrastructure.grpc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.recommendation.api.library.v1.*;
import com.silenteight.recommendation.api.library.v1.AlertOut.AlertStatus;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

@Slf4j
class RecommendationServiceClientMock implements RecommendationServiceClient {

  @Override
  public RecommendationsOut getRecommendations(RecommendationsIn request) {
    log.info("MOCK: Get recommendations called for analysis id: {}", request.getAnalysisName());
    return RecommendationsOut.builder()
        .recommendations(List.of(
            RecommendationOut.builder()
                .name("some_name")
                .recommendedAction("some_action")
                .recommendationComment("some_comment")
                .recommendedAt(OffsetDateTime.of(2022, 1, 19, 14, 30, 30, 0, ZoneOffset.UTC))
                .alert(AlertOut.builder()
                    .id("some_alert_id")
                    .status(AlertStatus.SUCCESS)
                    .metadata("{}")
                    .errorMessage("some_error_message")
                    .build())
                .matches(List.of(
                    MatchOut.builder()
                        .id("some_match_id")
                        .recommendedAction("match_action")
                        .recommendationComment("match_comment")
                        .stepId("step_id")
                        .fvSignature("fv_signature")
                        .features(Map.of(
                            "name", "EXACT_MATCH"
                        ))
                        .build()
                ))
                .build()
        ))
        .statistics(StatisticsOut.builder()
            .totalProcessedCount(5)
            .totalUnableToProcessCount(0)
            .recommendedAlertsCount(0)
            .recommendationsStatistics(RecommendationsStatisticsOut.builder()
                .truePositiveCount(5)
                .falsePositiveCount(0)
                .manualInvestigationCount(0)
                .errorCount(0)
                .build())
            .build())
        .build();
  }
}
