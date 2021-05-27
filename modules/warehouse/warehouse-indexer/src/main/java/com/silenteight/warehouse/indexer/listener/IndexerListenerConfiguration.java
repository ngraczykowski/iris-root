package com.silenteight.warehouse.indexer.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.warehouse.common.environment.EnvironmentProperties;
import com.silenteight.warehouse.indexer.IndexerIntegrationProperties;
import com.silenteight.warehouse.indexer.analysis.ProductionNamingStrategy;
import com.silenteight.warehouse.indexer.analysis.SimulationNamingStrategy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import javax.validation.Valid;

import static com.silenteight.warehouse.indexer.gateway.IndexerGatewayConfiguration.PRODUCTION_INDEXED_OUTBOUND_CHANNEL;
import static com.silenteight.warehouse.indexer.gateway.IndexerGatewayConfiguration.SIMULATION_INDEXED_OUTBOUND_CHANNEL;

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
  private final IndexerIntegrationProperties properties;

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
      IndexRequestCommandHandler indexRequestCommand,
      @Valid EnvironmentProperties environmentProperties) {

    String environmentPrefix = environmentProperties.getPrefix();
    ProductionNamingStrategy namingStrategy = new ProductionNamingStrategy(environmentPrefix);
    return new ProductionRequestCommandIntegrationFlowAdapter(indexRequestCommand, namingStrategy,
        PRODUCTION_INDEXING_INBOUND_CHANNEL, PRODUCTION_INDEXED_OUTBOUND_CHANNEL);
  }

  @Bean
  SimulationRequestCommandIntegrationFlowAdapter simulationRequestCommandIntegrationFlowAdapter(
      IndexRequestCommandHandler indexRequestCommand,
      @Valid EnvironmentProperties environmentProperties) {

    String environmentPrefix = environmentProperties.getPrefix();
    SimulationNamingStrategy namingStrategy = new SimulationNamingStrategy(environmentPrefix);
    return new SimulationRequestCommandIntegrationFlowAdapter(indexRequestCommand, namingStrategy,
        SIMULATION_INDEXING_INBOUND_CHANNEL, SIMULATION_INDEXED_OUTBOUND_CHANNEL);
  }
}
