package com.silenteight.adjudication.engine.analysis.agentresponse.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.agentresponse.AgentResponseFacade;
import com.silenteight.adjudication.engine.common.integration.NoCorrelationIdException;
import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.silenteight.adjudication.engine.analysis.agentresponse.integration.AgentResponseChannels.AGENT_RESPONSE_INBOUND_CHANNEL;
import static com.silenteight.adjudication.engine.analysis.agentresponse.integration.AgentResponseChannels.MATCH_FEATURES_UPDATED_OUTBOUND_CHANNEL;
import static org.springframework.integration.IntegrationMessageHeaderAccessor.CORRELATION_ID;

@RequiredArgsConstructor
@Component
@Slf4j
@ConditionalOnProperty(
    value = "ae.solving.enabled",
    havingValue = "false",
    matchIfMissing = true
)
class ReceiveAgentExchangeResponseIntegrationFlow extends IntegrationFlowAdapter {

  private final AgentResponseFacade facade;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(AGENT_RESPONSE_INBOUND_CHANNEL)
        .handle(AgentExchangeResponse.class, this::handleResponse)
        .channel(MATCH_FEATURES_UPDATED_OUTBOUND_CHANNEL);
  }

  private Object handleResponse(AgentExchangeResponse payload, MessageHeaders headers) {
    var correlationId = headers.get(CORRELATION_ID, String.class);
    if (correlationId == null) {
      log.debug("Correlation ID is missing: headers={}", headers);
      throw new NoCorrelationIdException();
    }

    var requestId = UUID.fromString(correlationId);
    var result = facade.receiveAgentExchangeResponse(requestId, payload);

    return result.orElse(null);
  }
}
