package com.silenteight.adjudication.engine.analysis.recommendation.domain;

import lombok.Builder;
import lombok.Value;

import com.fasterxml.jackson.databind.node.ObjectNode;

@Value
@Builder
public class InsertRecommendationRequest {

  long alertId;

  long analysisId;

  long recommendationId;

  String recommendedAction;

  long[] matchIds;

  ObjectNode[] matchContexts;

  String comment;

  public static InsertRecommendationRequest fromAlertSolution(
      long analysisId, AlertSolution alertSolution) {
    return InsertRecommendationRequest
        .builder()
        .alertId(alertSolution.getAlertId())
        .recommendedAction(alertSolution.getRecommendedAction())
        .analysisId(analysisId)
        .matchContexts(alertSolution.getMatchContexts())
        .matchIds(alertSolution.getMatchIds())
        .comment(alertSolution.getComment())
        .build();
  }
}
