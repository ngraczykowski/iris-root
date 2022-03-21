package com.silenteight.adjudication.engine.analysis.matchrecommendation.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.matchrecommendation.MatchRecommendationFacade;
import com.silenteight.adjudication.internal.v1.MatchesSolved;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.stereotype.Component;

import static com.silenteight.adjudication.engine.analysis.matchrecommendation.integration.MatchRecommendationChannels.MATCH_RECOMMENDATIONS_GENERATED_OUTBOUND_CHANNEL;
import static com.silenteight.adjudication.engine.analysis.matchsolution.integration.MatchSolutionChannels.MATCHES_SOLVED_OUTBOUND_CHANNEL;

@RequiredArgsConstructor
@Component
@Slf4j
public class MatchesSolvedIntegrationFlow extends IntegrationFlowAdapter {

  private final MatchRecommendationFacade matchRecommendationFacade;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(MATCHES_SOLVED_OUTBOUND_CHANNEL)
        .handle(MatchesSolved.class, (p, h) -> {
          log.debug("Received solved matches for generating match recommendation = {}", p);
          var matchRecommendation = matchRecommendationFacade.generateMatchRecommendation(p);
          return matchRecommendation.orElse(null);
        })
        .channel(MATCH_RECOMMENDATIONS_GENERATED_OUTBOUND_CHANNEL);
  }
}
