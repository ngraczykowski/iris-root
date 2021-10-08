package com.silenteight.payments.bridge.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;

/**
 * The recommendation has been generated and is waiting for completing.
 */
@RequiredArgsConstructor
@Getter
@ToString
public class RecommendationGeneratedEvent extends DomainEvent {

  private final RecommendationsGenerated recommendationsGenerated;

}
