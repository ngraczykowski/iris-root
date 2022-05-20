package com.silenteight.adjudication.engine.analysis.matchsolution.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.matchsolution.MatchSolutionFacade;
import com.silenteight.adjudication.internal.v1.MatchCategoriesUpdated;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.stereotype.Component;

import static com.silenteight.adjudication.engine.analysis.categoryrequest.integration.CategoryRequestChannels.MATCH_CATEGORIES_UPDATED_OUTBOUND_CHANNEL;
import static com.silenteight.adjudication.engine.analysis.matchsolution.integration.MatchSolutionChannels.MATCHES_SOLVED_OUTBOUND_CHANNEL;

@RequiredArgsConstructor
@Component
public class MatchCategoriesUpdatedIntegrationFlow extends IntegrationFlowAdapter {

  private final MatchSolutionFacade facade;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(MATCH_CATEGORIES_UPDATED_OUTBOUND_CHANNEL)
        .handle(MatchCategoriesUpdated.class, (p, h) -> {
          var solvedMatches = facade.handleMatchCategoriesUpdated(p);
          return solvedMatches.orElse(null);
        })
        .channel(MATCHES_SOLVED_OUTBOUND_CHANNEL);
  }
}
