package com.silenteight.adjudication.engine.analysis.recommendation.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;

@Value
@Builder
public class RecommendationResponse {

  long recommendationId;

  long alertId;

  long analysisId;

  public RecommendationInfo toRecommendationInfo() {
    return RecommendationInfo
        .newBuilder()
        .setAlert("alerts/" + getAlertId())
        .setRecommendation(
            "analysis/" + getAnalysisId() + "/recommendations/" + getRecommendationId())
        .build();
  }
}
