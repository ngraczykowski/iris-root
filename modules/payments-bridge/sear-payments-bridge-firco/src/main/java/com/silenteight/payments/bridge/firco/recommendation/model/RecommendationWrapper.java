package com.silenteight.payments.bridge.firco.recommendation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v2.RecommendationMetadata;
import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;

import java.util.UUID;

import static com.silenteight.payments.bridge.firco.recommendation.model.RecommendationSource.AE;
import static com.silenteight.payments.bridge.firco.recommendation.model.RecommendationSource.BRIDGE;

@Data
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
public class RecommendationWrapper {

  private final UUID alertId;

  private RecommendationWithMetadata recommendationWithMetadata;

  public RecommendationSource getSource() {
    return hasRecommendation() ? AE : BRIDGE;
  }

  public boolean hasRecommendation() {
    return recommendationWithMetadata != null;
  }

  public RecommendationMetadata getMetadata() {
    return recommendationWithMetadata.getMetadata();
  }
}
