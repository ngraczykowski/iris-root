package com.silenteight.adjudication.engine.analysis.service.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.agentexchange.domain.AgentExchangeRequestMessage;
import com.silenteight.adjudication.engine.analysis.agentexchange.integration.AgentExchangeChannels;
import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;
import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.integration.amqp.dsl.AmqpBaseOutboundEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.integration.expression.FunctionExpression;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

import static com.google.common.base.Strings.nullToEmpty;

@SuppressWarnings({ "MethodMayBeStatic", "java:S2325" })
@RequiredArgsConstructor
@Component
class AgentExchangeRequestOutboundAmqpIntegrationFlow extends IntegrationFlowAdapter {

  private static final String AGENT_CONFIG_HEADER = "agentConfig";

  @Valid
  private final AnalysisOutboundAmqpIntegrationProperties properties;

  private final AmqpOutboundFactory outboundFactory;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(AgentExchangeChannels.AGENT_EXCHANGE_REQUEST_OUTBOUND_CHANNEL)
        .enrichHeaders(enricher -> enricher
            .correlationIdFunction(this::getCorrelationId, Boolean.TRUE)
            .priorityFunction(this::getPriority, Boolean.TRUE)
            .headerFunction(AGENT_CONFIG_HEADER, this::getAgentConfig))
        .<AgentExchangeRequestMessage, AgentExchangeRequest>transform(
            AgentExchangeRequestMessage::toRequest)
        .handle(createOutboundAdapter(properties.getAgent().getOutboundExchangeName()));
  }

  private String getCorrelationId(Message<AgentExchangeRequestMessage> message) {
    return message.getPayload().getRequestId().toString();
  }

  private Integer getPriority(Message<AgentExchangeRequestMessage> message) {
    return message.getPayload().getPriority();
  }

  private String getAgentConfig(Message<AgentExchangeRequestMessage> message) {
    return message.getPayload().getAgentConfig();
  }

  private AmqpBaseOutboundEndpointSpec<?, ?> createOutboundAdapter(
      String outboundExchangeName) {

    return outboundFactory
        .outboundAdapter()
        .exchangeName(outboundExchangeName)
        .routingKeyExpression(new FunctionExpression<>(
            AgentExchangeRequestOutboundAmqpIntegrationFlow::makeRoutingKey));
  }

  private static String makeRoutingKey(Message<?> message) {
    var agentConfig = nullToEmpty(message.getHeaders().get(AGENT_CONFIG_HEADER, String.class));
    return agentConfig.replace('.', '_').replace('/', '.');
  }
}
