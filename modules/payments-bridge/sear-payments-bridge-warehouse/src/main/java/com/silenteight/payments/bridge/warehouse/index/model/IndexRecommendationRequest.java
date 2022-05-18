package com.silenteight.payments.bridge.warehouse.index.model;

import lombok.Builder;
import lombok.Value;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v2.RecommendationMetadata;
import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;

@Value
@Builder
public class IndexRecommendationRequest {

  String discriminator;

  String systemId;

  RecommendationWithMetadata recommendationWithMetadata;

  public Recommendation getRecommendation() {
    return recommendationWithMetadata.getRecommendation();
  }

  public RecommendationMetadata getMetadata() {
    return recommendationWithMetadata.getMetadata();
  }
}
