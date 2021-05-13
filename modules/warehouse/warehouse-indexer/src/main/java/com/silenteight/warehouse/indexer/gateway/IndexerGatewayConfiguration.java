package com.silenteight.warehouse.indexer.gateway;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;
import com.silenteight.warehouse.indexer.IndexerIntegrationProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

@Configuration
@RequiredArgsConstructor
public class IndexerGatewayConfiguration {

  public static final String PRODUCTION_INDEXED_OUTBOUND_CHANNEL =
      "productionIndexedOutboundChannel";
  public static final String SIMULATION_INDEXED_OUTBOUND_CHANNEL =
      "simulationIndexedOutboundChannel";

  @NonNull
  private final AmqpOutboundFactory outboundFactory;
  @NonNull
  private final IndexerIntegrationProperties properties;

  @Bean
  IntegrationFlow productionIndexedOutboundChannelToExchangeIntegrationFlow() {
    return createOutputFlow(
        PRODUCTION_INDEXED_OUTBOUND_CHANNEL,
        properties.getProductionIndexedOutbound().getExchangeName(),
        properties.getProductionIndexedOutbound().getRoutingKey());
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
