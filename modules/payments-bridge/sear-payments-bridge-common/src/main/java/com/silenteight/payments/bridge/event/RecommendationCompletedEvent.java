package com.silenteight.payments.bridge.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;
import com.silenteight.payments.bridge.common.model.AlertId;

import java.util.Optional;
import java.util.UUID;

/**
 * The recommendation is completed and ready to be sent to requesting party.
 */
@Getter
@AllArgsConstructor
public class RecommendationCompletedEvent extends DomainEvent implements AlertId {

  private final RecommendationWithMetadata recommendationWithMetadata;
  @ToString.Include
  private final UUID alertId;

  public RecommendationCompletedEvent(UUID alertId) {
    this.alertId = alertId;
    this.recommendationWithMetadata = null;
  }

  public Optional<RecommendationWithMetadata> getRecommendation() {
    return Optional.ofNullable(recommendationWithMetadata);
  }

  public Optional<String> getAlertName() {
    return getRecommendation().map(r -> r.getRecommendation().getAlert());
  }
}
