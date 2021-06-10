package com.silenteight.adjudication.engine.analysis.matchsolution.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.matchsolution.MatchSolutionFacade;
import com.silenteight.adjudication.internal.v1.MatchFeaturesUpdated;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.stereotype.Component;

import static com.silenteight.adjudication.engine.analysis.agentresponse.integration.AgentResponseChannels.MATCH_FEATURES_UPDATED_OUTBOUND_CHANNEL;
import static com.silenteight.adjudication.engine.analysis.matchsolution.integration.MatchSolutionChannels.MATCHES_SOLVED_OUTBOUND_CHANNEL;

@RequiredArgsConstructor
@Component
public class MatchFeaturesUpdatedIntegrationFlow extends IntegrationFlowAdapter {

  private final MatchSolutionFacade facade;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(MATCH_FEATURES_UPDATED_OUTBOUND_CHANNEL)
        .handle(MatchFeaturesUpdated.class, (p, h) -> facade.handleMatchFeaturesUpdated(p))
        .split()
        .channel(MATCHES_SOLVED_OUTBOUND_CHANNEL);
  }
}
