package com.silenteight.agent.facade.exchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.agent.common.messaging.amqp.AmqpInboundFactory;
import com.silenteight.agent.common.messaging.amqp.AmqpOutboundFactory;
import com.silenteight.agent.facade.AgentFacade;
import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;
import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.dsl.context.IntegrationFlowContext;

import java.util.List;
import javax.annotation.PostConstruct;

import static com.silenteight.agent.facade.exchange.AgentFacadeConfiguration.INBOUND_CHANNEL_NAME;
import static com.silenteight.agent.facade.exchange.AgentFacadeConfiguration.OUTBOUND_CHANNEL_NAME;
import static org.springframework.integration.IntegrationMessageHeaderAccessor.CORRELATION_ID;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty({ "agent.facade.enabled", "facade.amqp.multi-queues.enabled" })
@EnableConfigurationProperties(AgentFacadeProperties.class)
@Slf4j
class MultiQueueIntegrationConfiguration {

  private final AgentFacadeProperties agentFacadeProperties;
  private final AmqpInboundFactory inboundFactory;
  private final AmqpOutboundFactory outboundFactory;
  private final List<AgentFacade<AgentExchangeRequest, AgentExchangeResponse>> agentFacades;
  private final IntegrationFlowContext context;
  private final MessageTransformer messageTransformer;

  @PostConstruct
  public void registerIntegrationFlows() {
    agentFacades.forEach(this::registerIntegrationFlow);
  }

  private void registerIntegrationFlow(
      AgentFacade<AgentExchangeRequest, AgentExchangeResponse> agentFacade) {

    var facadeName = agentFacade.getFacadeName();
    var queueItem = getQueueItem(facadeName);
    var inboundChannelName = INBOUND_CHANNEL_NAME + facadeName;
    var outboundChannelName = OUTBOUND_CHANNEL_NAME + facadeName;

    var requestFlow = createRequestFlow(queueItem.getInboundQueueName(), inboundChannelName);
    var processingFlow = createProcessingFlow(agentFacade, inboundChannelName, outboundChannelName);
    var responseFlow = createResponseFlow(queueItem.getOutboundExchangeName(), outboundChannelName);

    var requestFlowBuilder = context.registration(requestFlow);
    var processingFlowBuilder = context.registration(processingFlow);
    var responseFlowBuilder = context.registration(responseFlow);

    requestFlowBuilder.register();
    processingFlowBuilder.register();
    responseFlowBuilder.register();
  }

  private StandardIntegrationFlow createRequestFlow(String queueName, String channelName) {
    return IntegrationFlows
        .from(inboundFactory
            .simpleAdapter()
            .configureContainer(c -> c.addQueueNames(queueName)))
        .channel(channelName)
        .get();
  }

  private StandardIntegrationFlow createProcessingFlow(
      AgentFacade<AgentExchangeRequest, AgentExchangeResponse> agentFacade,
      String inChannelName, String outChannelName) {

    return IntegrationFlows
        .from(inChannelName)
        .transform(messageTransformer)
        .handle(
            AgentExchangeRequest.class,
            (payload, headers) -> agentFacade.processMessage(payload))
        .enrichHeaders(enricher -> enricher.correlationIdFunction(
            message -> message.getHeaders().get(CORRELATION_ID), Boolean.TRUE))
        .channel(outChannelName)
        .get();
  }

  private StandardIntegrationFlow createResponseFlow(String exchangeName, String channelName) {
    return IntegrationFlows
        .from(channelName)
        .handle(outboundFactory
            .outboundAdapter()
            .exchangeName(exchangeName))
        .get();
  }

  private QueueItem getQueueItem(String facadeName) {
    return agentFacadeProperties.getQueueDefinitionByFacadeName(facadeName);
  }
}
