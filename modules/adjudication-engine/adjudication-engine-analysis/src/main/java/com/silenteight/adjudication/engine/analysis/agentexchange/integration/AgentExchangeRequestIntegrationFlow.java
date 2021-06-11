package com.silenteight.adjudication.engine.analysis.agentexchange.integration;

import com.silenteight.adjudication.engine.analysis.agentexchange.domain.AgentExchangeRequestMessage;
import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static com.silenteight.adjudication.engine.analysis.agentexchange.integration.AgentExchangeChannels.AGENT_CONFIG_HEADER;
import static com.silenteight.adjudication.engine.analysis.agentexchange.integration.AgentExchangeChannels.AGENT_EXCHANGE_REQUEST_GATEWAY_CHANNEL;
import static com.silenteight.adjudication.engine.analysis.agentexchange.integration.AgentExchangeChannels.AGENT_EXCHANGE_REQUEST_OUTBOUND_CHANNEL;

@SuppressWarnings({ "MethodMayBeStatic", "java:S2325" })
@Component
class AgentExchangeRequestIntegrationFlow extends IntegrationFlowAdapter {

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(AGENT_EXCHANGE_REQUEST_GATEWAY_CHANNEL)
        .enrichHeaders(enricher -> enricher
            .correlationIdFunction(this::getCorrelationId, Boolean.TRUE)
            .priorityFunction(this::getPriority, Boolean.TRUE)
            .headerFunction(AGENT_CONFIG_HEADER, this::getAgentConfig))
        .<AgentExchangeRequestMessage, AgentExchangeRequest>transform(
            AgentExchangeRequestMessage::toRequest)
        .channel(AGENT_EXCHANGE_REQUEST_OUTBOUND_CHANNEL);
  }

  private String getCorrelationId(Message<AgentExchangeRequestMessage> message) {
    return message.getPayload().getCorrelationId();
  }

  private Integer getPriority(Message<AgentExchangeRequestMessage> message) {
    return message.getPayload().getPriority();
  }

  private String getAgentConfig(Message<AgentExchangeRequestMessage> message) {
    return message.getPayload().getAgentConfig();
  }

}
