package com.silenteight.adjudication.engine.analysis.agentexchange.integration;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.internal.v1.PendingRecommendations;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(
    value = "ae.analysis.agent-exchange.enabled",
    havingValue = "false"
)
class DummyRequestMissingFeatureValuesIntegrationFlow extends IntegrationFlowAdapter {

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(AgentExchangeChannels.AGENT_EXCHANGE_PENDING_RECOMMENDATIONS_INBOUND_CHANNEL)
        .handle(PendingRecommendations.class, (payload, headers) -> {
          log.warn("Disabled old solving process! Using new solving!");
          return null;
        });
  }
}
