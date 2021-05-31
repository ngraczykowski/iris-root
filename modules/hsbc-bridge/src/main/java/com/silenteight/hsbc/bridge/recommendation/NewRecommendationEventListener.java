package com.silenteight.hsbc.bridge.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.recommendation.event.NewRecommendationsEvent;

import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
@Slf4j
class NewRecommendationEventListener {

  private final RecommendationHandler recommendationHandler;

  @EventListener
  public void onNewRecommendationsEvent(NewRecommendationsEvent event) {
    recommendationHandler.getAndStoreRecommendations(event.getAnalysis(), event.getDataset());
  }
}
