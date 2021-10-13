package com.silenteight.payments.bridge.event;

import lombok.Getter;
import lombok.ToString;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.payments.bridge.common.model.AlertId;

import java.util.Optional;
import java.util.UUID;

/**
 * The recommendation is completed and ready to be sent to requesting party.
 */
@Getter
public class RecommendationCompletedEvent extends DomainEvent implements AlertId {

  private final Recommendation recommendation;
  @ToString.Include
  private final UUID alertId;

  public RecommendationCompletedEvent(Recommendation recommendation) {
    this.recommendation = recommendation;
    this.alertId = UUID.fromString(recommendation.getAlert());
  }

  public RecommendationCompletedEvent(UUID alertId) {
    this.alertId = alertId;
    this.recommendation = null;
  }

  public Optional<Recommendation> getRecommendation() {
    return Optional.ofNullable(recommendation);
  }


}
