package com.silenteight.adjudication.engine.analysis.recommendation.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.matchsolution.dto.MatchesSolvedEvent;
import com.silenteight.adjudication.engine.analysis.recommendation.RecommendationFacade;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
class MatchesSolvedRecommendationIntegrationFlow {

  private final RecommendationFacade facade;

  @Async
  @EventListener
  public void receiveSolvedMatches(MatchesSolvedEvent event) {
    var matchesSolved = event.getMatchesSolved();
    log.debug("Received solved matches for generating recommendation = {}", matchesSolved);
    var recommendation = facade.handleMatchesSolved(matchesSolved);
    if (recommendation.isEmpty()) {
      log.info("MatchesSolvedRecommendationIntegrationFlow recommendation empty");
    }
  }
}
