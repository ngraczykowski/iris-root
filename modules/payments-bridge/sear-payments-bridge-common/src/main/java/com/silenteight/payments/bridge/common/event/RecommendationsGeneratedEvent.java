package com.silenteight.payments.bridge.common.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;

@RequiredArgsConstructor
@Getter
public class RecommendationsGeneratedEvent {
  private final RecommendationsGenerated recommendationsGenerated;
}
