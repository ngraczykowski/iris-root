package com.silenteight.warehouse.indexer.indexing.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.warehouse.indexer.indexing.IndexerProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import static com.silenteight.warehouse.indexer.indexing.gateway.IndexerGatewayConfiguration.PRODUCTION_INDEXED_OUTBOUND_CHANNEL;
import static com.silenteight.warehouse.indexer.indexing.gateway.IndexerGatewayConfiguration.SIMULATION_INDEXED_OUTBOUND_CHANNEL;

@Configuration
@RequiredArgsConstructor
class IndexerListenerConfiguration {

  public static final String SIMULATION_INDEXING_INBOUND_CHANNEL =
      "simulationIndexingInboundChannel";
  public static final String PRODUCTION_INDEXING_INBOUND_CHANNEL =
      "productionIndexingInboundChannel";

  @NonNull
  private final AmqpInboundFactory inboundFactory;

  @NonNull
  private final IndexerProperties properties;

  @Bean
  IntegrationFlow productionIndexingQueueToChannelIntegrationFlow() {
    return createInputFlow(
        PRODUCTION_INDEXING_INBOUND_CHANNEL,
        properties.getProductionIndexingInbound().getQueueName());
  }

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
  ProductionRequestCommandIntegrationFlowAdapter productionRequestCommandIntegrationFlowAdapter(
      ProductionIndexRequestCommandHandler productionIndexRequestCommandHandler) {

    return new ProductionRequestCommandIntegrationFlowAdapter(
        productionIndexRequestCommandHandler,
        PRODUCTION_INDEXING_INBOUND_CHANNEL,
        PRODUCTION_INDEXED_OUTBOUND_CHANNEL);
  }

  @Bean
  SimulationRequestCommandIntegrationFlowAdapter simulationRequestCommandIntegrationFlowAdapter(
      SimulationIndexRequestCommandHandler simulationIndexRequestCommandHandler) {

    return new SimulationRequestCommandIntegrationFlowAdapter(
        simulationIndexRequestCommandHandler,
        SIMULATION_INDEXING_INBOUND_CHANNEL,
        SIMULATION_INDEXED_OUTBOUND_CHANNEL);
  }
}
