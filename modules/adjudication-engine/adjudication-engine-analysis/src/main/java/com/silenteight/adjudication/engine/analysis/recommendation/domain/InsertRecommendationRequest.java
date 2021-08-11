package com.silenteight.adjudication.engine.analysis.recommendation.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InsertRecommendationRequest {

  long alertId;

  long analysisId;

  long recommendationId;

  String recommendedAction;

  public static InsertRecommendationRequest fromAlertSolution(
      long analysisId, AlertSolution alertSolution) {
    return InsertRecommendationRequest
        .builder()
        .alertId(alertSolution.getAlertId())
        .recommendedAction(alertSolution.getRecommendedAction())
        .analysisId(analysisId)
        .build();
  }
}
