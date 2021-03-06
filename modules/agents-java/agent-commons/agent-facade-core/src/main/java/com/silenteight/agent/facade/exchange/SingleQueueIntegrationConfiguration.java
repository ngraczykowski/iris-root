package com.silenteight.agent.facade.exchange;

import lombok.RequiredArgsConstructor;

import com.silenteight.agent.common.messaging.amqp.AmqpInboundFactory;
import com.silenteight.agent.common.messaging.amqp.AmqpOutboundFactory;
import com.silenteight.agent.facade.AgentFacade;
import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;
import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import static com.silenteight.agent.facade.exchange.AgentFacadeConfiguration.INBOUND_CHANNEL_NAME;
import static com.silenteight.agent.facade.exchange.AgentFacadeConfiguration.OUTBOUND_CHANNEL_NAME;
import static com.silenteight.agent.facade.exchange.ConfigurationNameGenerator.getConfigurationNames;
import static com.silenteight.agent.facade.exchange.DeleteQueueWithoutPrioritySupportUseCase.deleteIfEmptyQueueWithoutPrioritySupport;
import static org.springframework.amqp.support.AmqpHeaders.RECEIVED_ROUTING_KEY;
import static org.springframework.integration.IntegrationMessageHeaderAccessor.CORRELATION_ID;

@Configuration
@RequiredArgsConstructor
@Conditional(SingleFacadeEnabledCondition.class)
@EnableConfigurationProperties(AgentFacadeProperties.class)
class SingleQueueIntegrationConfiguration {

  private final AgentFacadeProperties agentFacadeProperties;
  private final AmqpInboundFactory inboundFactory;
  private final AmqpOutboundFactory outboundFactory;
  private final AgentFacade<AgentExchangeRequest, AgentExchangeResponse> agentFacade;
  private final AmqpAdmin amqpAdmin;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Bean
  IntegrationFlow requestFlow(
      @Qualifier("facadeInQueueBinding")
          ObjectProvider<Binding> queueWithoutPrioritySupportBinding) {
    return IntegrationFlows
        .from(inboundFactory
            .simpleAdapter()
            .configureContainer(c -> {
              c.addQueueNames(agentFacadeProperties.getInboundQueueWithPrioritySupportName());
              queueWithoutPrioritySupportBinding.ifAvailable(amqpAdmin::removeBinding);
              deleteIfEmptyQueueWithoutPrioritySupport(
                  amqpAdmin, agentFacadeProperties.getInboundQueueName(), c);
            }))
        .channel(INBOUND_CHANNEL_NAME)
        .get();
  }

  @Bean
  IntegrationFlow processingFlow(MessageTransformer messageTransformer) {
    return IntegrationFlows
        .from(INBOUND_CHANNEL_NAME)
        .transform(messageTransformer)
        .handle(
            AgentExchangeRequest.class,
            (payload, headers) -> agentFacade.processMessage(
                payload,
                getConfigurationNames(
                    payload.getFeaturesList(),
                    headers.get(RECEIVED_ROUTING_KEY, String.class))))
        .enrichHeaders(enricher -> enricher.correlationIdFunction(
            message -> message.getHeaders().get(CORRELATION_ID), Boolean.TRUE))
        .channel(OUTBOUND_CHANNEL_NAME)
        .get();
  }

  @Bean
  IntegrationFlow responseFlow() {
    return IntegrationFlows
        .from(OUTBOUND_CHANNEL_NAME)
        .handle(outboundFactory
            .outboundAdapter()
            .exchangeName(agentFacadeProperties.getOutboundExchangeName()))
        .get();
  }
}
