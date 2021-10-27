package com.silenteight.warehouse.indexer.simulation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.alert.indexing.AlertIndexService;
import com.silenteight.warehouse.indexer.alert.mapping.AlertMapper;
import com.silenteight.warehouse.indexer.match.mapping.MatchMapper;
import com.silenteight.warehouse.indexer.query.single.AlertSearchService;
import com.silenteight.warehouse.indexer.query.single.ProductionSearchRequestBuilder;
import com.silenteight.warehouse.indexer.simulation.analysis.UniqueAnalysisFactory;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

@Configuration
@EnableConfigurationProperties(SimulationIndexerProperties.class)
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
  private final SimulationIndexerProperties properties;

  @Bean
  SimulationAlertIndexUseCase simulationAlertIndexUseCase(
      SimulationAlertMappingService simulationAlertMappingService,
      AlertIndexService alertIndexService,
      UniqueAnalysisFactory uniqueAnalysisFactory,
      TimeSource timeSource) {

    return new SimulationAlertIndexUseCase(
        simulationAlertMappingService,
        alertIndexService,
        uniqueAnalysisFactory,
        timeSource,
        properties.getSimulationBatchSize());
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
  SimulationRequestCommandIntegrationFlowAdapter simulationRequestCommandIntegrationFlowAdapter(
      SimulationIndexRequestCommandHandler simulationIndexRequestCommandHandler) {

    return new SimulationRequestCommandIntegrationFlowAdapter(
        simulationIndexRequestCommandHandler,
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

  @Bean
  SimulationAlertMappingService simulationAlertMappingService(
      AlertMapper alertMapper,
      MatchMapper matchMapper,
      RestHighLevelClient restHighLevelAdminClient,
      AlertSearchService alertSearchService,
      ProductionSearchRequestBuilder productionSearchRequestBuilder) {
    return new SimulationAlertMappingService(
        alertMapper,
        matchMapper,
        restHighLevelAdminClient,
        alertSearchService,
        productionSearchRequestBuilder);
  }
}
