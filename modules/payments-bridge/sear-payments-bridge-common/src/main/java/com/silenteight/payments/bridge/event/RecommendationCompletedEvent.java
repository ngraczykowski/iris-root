package com.silenteight.payments.bridge.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Recommendation;

/**
 * The recommendation is completed and ready to be sent to requesting party.
 */
@RequiredArgsConstructor
@Getter
public class RecommendationCompletedEvent extends DomainEvent {

  private final Recommendation recommendation;

}
