package com.silenteight.payments.bridge.firco.recommendation.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;
import com.silenteight.payments.bridge.common.model.AlertId;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class CreatedRecommendation implements AlertId {

  @ToString.Include
  private final UUID alertId;

  public static CreatedRecommendation fromBridge(
      UUID alertId, String status, String reason) {
    return new BridgeSourcedRecommendation(alertId, status, reason);
  }

  public static CreatedRecommendation fromAdjudication(UUID alertId,
      RecommendationWithMetadata recommendation) {
    return new AdjudicationEngineSourcedRecommendation(alertId, recommendation);
  }

  @Getter
  public static class BridgeSourcedRecommendation extends CreatedRecommendation {
    private final String status;
    private final String reason;

    public BridgeSourcedRecommendation(UUID alertId, String status, String reason) {
      super(alertId);
      this.status = status;
      this.reason = reason;
    }
  }

  @Getter
  public static class AdjudicationEngineSourcedRecommendation
      extends CreatedRecommendation {

    private final RecommendationWithMetadata recommendation;

    public AdjudicationEngineSourcedRecommendation(UUID alertId,
        RecommendationWithMetadata recommendationWithMetadata) {
      super(alertId);
      this.recommendation = recommendationWithMetadata;
    }

    public String getAlertName() {
      return getRecommendation().getRecommendation().getAlert();
    }
  }
}
