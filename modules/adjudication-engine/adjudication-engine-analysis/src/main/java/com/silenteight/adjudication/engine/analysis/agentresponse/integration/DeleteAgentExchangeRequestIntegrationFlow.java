package com.silenteight.adjudication.engine.analysis.agentresponse.integration;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.internal.v1.AgentResponseStored;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import static com.silenteight.adjudication.engine.analysis.agentresponse.integration.AgentResponseChannels.DELETE_AGENT_EXCHANGE_GATEWAY_CHANNEL;
import static com.silenteight.adjudication.engine.analysis.agentresponse.integration.AgentResponseChannels.DELETE_AGENT_EXCHANGE_OUTBOUND_CHANNEL;

@SuppressWarnings({ "MethodMayBeStatic", "java:S2325" })
@Component
@Slf4j
class DeleteAgentExchangeRequestIntegrationFlow extends IntegrationFlowAdapter {

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(DELETE_AGENT_EXCHANGE_GATEWAY_CHANNEL)
        .handle(AgentResponseStored.class, this::logRequest)
        .channel(DELETE_AGENT_EXCHANGE_OUTBOUND_CHANNEL);
  }

  private AgentResponseStored logRequest(
      AgentResponseStored payload, MessageHeaders headers) {

    if (log.isDebugEnabled()) {
      log.debug("Sending delete agent exchange request");
    }

    return payload;
  }
}
