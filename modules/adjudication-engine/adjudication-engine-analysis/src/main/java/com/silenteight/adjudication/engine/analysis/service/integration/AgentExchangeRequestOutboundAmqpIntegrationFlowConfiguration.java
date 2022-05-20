package com.silenteight.adjudication.engine.analysis.service.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.agentexchange.integration.AgentExchangeChannels;
import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpBaseOutboundEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.expression.FunctionExpression;
import org.springframework.messaging.Message;

import javax.validation.Valid;

import static com.google.common.base.Strings.nullToEmpty;
import static com.silenteight.adjudication.engine.analysis.agentexchange.integration.AgentExchangeChannels.AGENT_CONFIG_HEADER;
import static org.springframework.integration.dsl.IntegrationFlows.from;

@RequiredArgsConstructor
@Configuration
class AgentExchangeRequestOutboundAmqpIntegrationFlowConfiguration {

  @Valid
  private final AnalysisOutboundAmqpIntegrationProperties properties;

  private final AmqpOutboundFactory outboundFactory;

  @Bean
  @ConditionalOnProperty(prefix = "ae.analysis.integration.outbound.agent", name = "enabled",
      havingValue = "true", matchIfMissing = true)
  IntegrationFlow agentExchangeRequestOutboundAmqpIntegrationFlow() {
    return from(AgentExchangeChannels.AGENT_EXCHANGE_REQUEST_OUTBOUND_CHANNEL)
        .handle(createOutboundAdapter(properties.getAgent().getOutboundExchangeName()))
        .get();
  }

  private AmqpBaseOutboundEndpointSpec<?, ?> createOutboundAdapter(
      String outboundExchangeName) {

    return outboundFactory
        .outboundAdapter()
        .exchangeName(outboundExchangeName)
        .routingKeyExpression(new FunctionExpression<>(
            AgentExchangeRequestOutboundAmqpIntegrationFlowConfiguration::makeRoutingKey));
  }

  private static String makeRoutingKey(Message<?> message) {
    var agentConfig = nullToEmpty(message.getHeaders().get(AGENT_CONFIG_HEADER, String.class));
    return agentConfig.replace('.', '_').replace('/', '.');
  }
}
