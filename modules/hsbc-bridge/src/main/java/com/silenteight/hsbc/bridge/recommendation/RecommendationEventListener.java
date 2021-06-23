package com.silenteight.hsbc.bridge.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.analysis.event.AnalysisCompletedEvent;

import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
@Slf4j
class RecommendationEventListener {

  private final RecommendationHandler recommendationHandler;

  @EventListener
  public void onAnalysisCompletedEvent(AnalysisCompletedEvent event) {
    log.debug("Handling AnalysisCompletedEvent, analysis={}", event.getAnalysis());

    recommendationHandler.getAndStoreRecommendations(event.getAnalysis());
  }
}
