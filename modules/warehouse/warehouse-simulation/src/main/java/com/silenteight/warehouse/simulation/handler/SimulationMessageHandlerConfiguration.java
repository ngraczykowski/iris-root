package com.silenteight.warehouse.simulation.handler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

@Configuration
@EnableConfigurationProperties(SimulationHandlerProperties.class)
@RequiredArgsConstructor
public class SimulationMessageHandlerConfiguration {

  public static final String SIMULATION_INDEXING_INBOUND_CHANNEL =
      "simulationIndexingInboundChannel";
  public static final String SIMULATION_INDEXED_OUTBOUND_CHANNEL =
      "simulationIndexedOutboundChannel";

  @NonNull
  private final AmqpInboundFactory inboundFactory;

  @NonNull
  private final AmqpOutboundFactory outboundFactory;

  @NonNull
  private final SimulationHandlerProperties properties;

  @Bean
  IntegrationFlow simulationIndexingQueueToChannelIntegrationFlow() {
    return createInputFlow(
        SIMULATION_INDEXING_INBOUND_CHANNEL,
        properties.getSimulationIndexingInbound().getQueueName());
  }

  private IntegrationFlow createInputFlow(String channel, String queue) {
    return IntegrationFlows
        .from(inboundFactory
            .simpleAdapter()
            .configureContainer(c -> c.addQueueNames(queue)))
        .channel(channel)
        .get();
  }

  @Bean
  SimulationRequestCommandIntegrationFlowAdapter simulationRequestCommandIntegrationFlowAdapter(
      SimulationRequestV1CommandHandler simulationRequestV1CommandHandler,
      SimulationRequestV2CommandHandler simulationRequestV2CommandHandler) {

    return new SimulationRequestCommandIntegrationFlowAdapter(
        simulationRequestV1CommandHandler,
        simulationRequestV2CommandHandler,
        SIMULATION_INDEXING_INBOUND_CHANNEL,
        SIMULATION_INDEXED_OUTBOUND_CHANNEL);
  }

  @Bean
  IntegrationFlow simulationIndexedOutboundChannelToExchangeIntegrationFlow() {
    return createOutputFlow(
        SIMULATION_INDEXED_OUTBOUND_CHANNEL,
        properties.getSimulationIndexedOutbound().getExchangeName(),
        properties.getSimulationIndexedOutbound().getRoutingKey());
  }

  private IntegrationFlow createOutputFlow(String channel, String exchange, String routingKey) {
    return flow -> flow
        .channel(channel)
        .handle(outboundFactory
            .outboundAdapter()
            .exchangeName(exchange)
            .routingKey(routingKey));
  }
}
