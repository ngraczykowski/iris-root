package com.silenteight.payments.bridge.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.payments.bridge.common.model.AlertId;

import java.util.UUID;

/**
 * The recommendation is completed and ready to be sent to requesting party.
 */
@RequiredArgsConstructor
@Getter
@ToString(onlyExplicitlyIncluded = true)
public class RecommendationCompletedEvent extends DomainEvent implements AlertId {

  private final Recommendation recommendation;

  @ToString.Include(name = "alertId")
  @Override
  public UUID getAlertId() {
    return UUID.fromString(recommendation.getAlert());
  }
}
