package com.silenteight.agent.facade.exchange;

import com.silenteight.agent.common.messaging.amqp.AmqpInboundFactory;
import com.silenteight.agent.common.messaging.amqp.AmqpOutboundFactory;
import com.silenteight.agent.facade.AgentFacade;
import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;
import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Declarables;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.SimpleMessageListenerContainerSpec;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.dsl.context.IntegrationFlowContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.PostConstruct;

import static com.silenteight.agent.facade.exchange.AgentFacadeConfiguration.INBOUND_CHANNEL_NAME;
import static com.silenteight.agent.facade.exchange.AgentFacadeConfiguration.OUTBOUND_CHANNEL_NAME;
import static com.silenteight.agent.facade.exchange.DeleteQueueWithoutPrioritySupportUseCase.deleteIfEmptyQueueWithoutPrioritySupport;
import static org.springframework.integration.IntegrationMessageHeaderAccessor.CORRELATION_ID;

@Configuration
@Conditional(MultiFacadeEnabledCondition.class)
@EnableConfigurationProperties(AgentFacadeProperties.class)
class MultiQueueIntegrationConfiguration {

  private final AgentFacadeProperties agentFacadeProperties;
  private final AmqpInboundFactory inboundFactory;
  private final AmqpOutboundFactory outboundFactory;
  private final List<AgentFacade<AgentExchangeRequest, AgentExchangeResponse>> agentFacades;
  private final IntegrationFlowContext context;
  private final MessageTransformer messageTransformer;
  private final AmqpAdmin amqpAdmin;
  private final Collection<Declarable> declarables;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  MultiQueueIntegrationConfiguration(
      AgentFacadeProperties agentFacadeProperties,
      AmqpInboundFactory inboundFactory,
      AmqpOutboundFactory outboundFactory,
      List<AgentFacade<AgentExchangeRequest, AgentExchangeResponse>> agentFacades,
      IntegrationFlowContext context,
      MessageTransformer messageTransformer, AmqpAdmin amqpAdmin,
      @Qualifier("multiFacadeDeclarables") ObjectProvider<Declarables> declarables) {
    this.agentFacadeProperties = agentFacadeProperties;
    this.inboundFactory = inboundFactory;
    this.outboundFactory = outboundFactory;
    this.agentFacades = agentFacades;
    this.context = context;
    this.messageTransformer = messageTransformer;
    this.amqpAdmin = amqpAdmin;
    this.declarables = new ArrayList<>();
    declarables.ifAvailable(d -> this.declarables.addAll(d.getDeclarables()));
  }

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

    var requestFlow = createRequestFlow(queueItem.getInboundQueueName(),
        queueItem.getInboundQueueWithPrioritySupportName(), inboundChannelName);
    var processingFlow = createProcessingFlow(agentFacade, inboundChannelName, outboundChannelName);
    var responseFlow = createResponseFlow(queueItem.getOutboundExchangeName(), outboundChannelName);

    var requestFlowBuilder = context.registration(requestFlow);
    var processingFlowBuilder = context.registration(processingFlow);
    var responseFlowBuilder = context.registration(responseFlow);

    requestFlowBuilder.register();
    processingFlowBuilder.register();
    responseFlowBuilder.register();
  }

  private StandardIntegrationFlow createRequestFlow(
      String queueName, String queueWithPrioritySupportName, String channelName) {
    return IntegrationFlows
        .from(inboundFactory
            .simpleAdapter()
            .configureContainer(baseOn(queueName, queueWithPrioritySupportName)))
        .channel(channelName)
        .get();
  }

  private Consumer<SimpleMessageListenerContainerSpec> baseOn(
      String queueName, String queueWithPrioritySupportName) {
    return c -> {
      c.addQueueNames(queueWithPrioritySupportName);
      unbindQueueWithoutPrioritySupport(queueName);
      deleteIfEmptyQueueWithoutPrioritySupport(amqpAdmin, queueName, c);
    };
  }

  private void unbindQueueWithoutPrioritySupport(String queueName) {
    declarables
        .stream()
        .filter(Binding.class::isInstance)
        .map(Binding.class::cast)
        .filter(binding -> binding.getDestination().equals(queueName))
        .findFirst()
        .ifPresent(amqpAdmin::removeBinding);
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
