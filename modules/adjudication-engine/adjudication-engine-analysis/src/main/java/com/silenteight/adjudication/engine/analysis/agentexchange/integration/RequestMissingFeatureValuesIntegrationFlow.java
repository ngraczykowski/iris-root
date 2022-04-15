package com.silenteight.adjudication.engine.analysis.agentexchange.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.agentexchange.AgentExchangeFacade;
import com.silenteight.adjudication.internal.v1.PendingRecommendations;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@ConditionalOnProperty(
    value = "ae.solving.enabled",
    havingValue = "false",
    matchIfMissing = true
)
class RequestMissingFeatureValuesIntegrationFlow extends IntegrationFlowAdapter {

  private final AgentExchangeFacade agentExchangeFacade;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(AgentExchangeChannels.AGENT_EXCHANGE_PENDING_RECOMMENDATIONS_INBOUND_CHANNEL)
        .handle(PendingRecommendations.class, (payload, headers) -> {
          payload.getAnalysisList().forEach(agentExchangeFacade::requestMissingFeatureValues);
          return null;
        });
  }
}
