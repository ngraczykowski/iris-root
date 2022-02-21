package com.silenteight.adjudication.engine.analysis.recommendation.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.api.v2.RecommendationMetadata;

@Value
@Builder
public class RecommendationResponse {

  long recommendationId;

  long alertId;

  long analysisId;

  RecommendationMetadata metaData;

  Recommendation recommendation;


  public RecommendationInfo toRecommendationInfo(
      final boolean shouldAttachMetaData, final boolean shouldAttachRecommendation) {
    final RecommendationInfo.Builder builder = RecommendationInfo
        .newBuilder()
        .setAlert("alerts/" + getAlertId())
        .setRecommendation(
            "analysis/" + getAnalysisId() + "/recommendations/" + getRecommendationId());

    if (shouldAttachMetaData) {
      builder.setMetadata(this.metaData);
    }

    if (shouldAttachRecommendation) {
      builder.setValue(this.recommendation);
    }

    return builder
        .build();
  }
}
