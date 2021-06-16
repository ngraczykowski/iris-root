package com.silenteight.adjudication.engine.analysis.recommendation.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.recommendation.RecommendationFacade;
import com.silenteight.adjudication.internal.v1.MatchesSolved;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class MatchesSolvedRecommendationIntegrationFlow extends IntegrationFlowAdapter {

  private final RecommendationFacade facade;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(RecommendationChannels.MATCHES_SOLVED_RECOMMENDATION_INBOUND_CHANNEL)
        .handle(MatchesSolved.class, (payload, headers) -> {
          var notification = facade.handleMatchesSolved(payload);
          return notification.orElse(null);
        })
        .log(Level.DEBUG, getClass().getName())
        .channel(RecommendationChannels.RECOMMENDATIONS_GENERATED_OUTBOUND_CHANNEL);
  }
}
