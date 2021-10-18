package com.silenteight.payments.bridge.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;
import com.silenteight.payments.bridge.common.model.AlertId;

import java.util.UUID;

/**
 * The recommendation is completed and ready to be sent to requesting party.
 */
@Getter
@AllArgsConstructor
public class RecommendationCompletedEvent extends DomainEvent implements AlertId {

  @ToString.Include
  private final UUID alertId;

  public static RecommendationCompletedEvent fromBridge(UUID alertId, String reason) {
    return new BridgeRecommendationCompletedEvent(alertId, reason);
  }

  public static RecommendationCompletedEvent fromAdjudication(UUID alertId,
      RecommendationWithMetadata recommendation) {
    return new AdjudicationRecommendationCompletedEvent(alertId, recommendation);
  }

  @Getter
  public static class BridgeRecommendationCompletedEvent extends RecommendationCompletedEvent {
    private final String reason;

    public BridgeRecommendationCompletedEvent(UUID alertId, String reason) {
      super(alertId);
      this.reason = reason;
    }
  }

  @Getter
  public static class AdjudicationRecommendationCompletedEvent
      extends RecommendationCompletedEvent {
    private final RecommendationWithMetadata recommendation;

    public AdjudicationRecommendationCompletedEvent(UUID alertId,
        RecommendationWithMetadata recommendationWithMetadata) {
      super(alertId);
      this.recommendation = recommendationWithMetadata;
    }

    public String getAlertName() {
      return getRecommendation().getRecommendation().getAlert();
    }
  }
}
