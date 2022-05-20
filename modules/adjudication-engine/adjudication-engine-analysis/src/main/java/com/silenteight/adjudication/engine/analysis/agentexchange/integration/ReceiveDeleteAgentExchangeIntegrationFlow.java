package com.silenteight.adjudication.engine.analysis.agentexchange.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.agentexchange.AgentExchangeFacade;
import com.silenteight.adjudication.internal.v1.AgentResponseStored;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import static com.silenteight.adjudication.engine.analysis.agentexchange.integration.AgentExchangeChannels.DELETE_AGENT_EXCHANGE_INBOUND_CHANNEL;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(
    value = "ae.solving.enabled",
    havingValue = "true"
)
class ReceiveDeleteAgentExchangeIntegrationFlow extends IntegrationFlowAdapter {

  private final AgentExchangeFacade agentExchangeFacade;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(DELETE_AGENT_EXCHANGE_INBOUND_CHANNEL)
        .log("Receiving delete agent exchange request")
        .handle(AgentResponseStored.class, this::deleteExchange);
  }

  private int deleteExchange(AgentResponseStored payload, MessageHeaders headers) {
    agentExchangeFacade.removeReceivedAgentExchanges(payload);
    return 0;
  }
}
