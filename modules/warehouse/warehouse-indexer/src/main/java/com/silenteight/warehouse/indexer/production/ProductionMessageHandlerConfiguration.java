package com.silenteight.warehouse.indexer.production;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;
import com.silenteight.warehouse.indexer.production.v1.ProductionIndexRequestV1CommandHandler;
import com.silenteight.warehouse.indexer.production.v2.ProductionIndexRequestV2CommandHandler;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

@Configuration
@EnableConfigurationProperties(ProductionIndexerProperties.class)
@RequiredArgsConstructor
class ProductionMessageHandlerConfiguration {

  public static final String PRODUCTION_INDEXING_INBOUND_CHANNEL =
      "productionIndexingInboundChannel";
  public static final String PRODUCTION_INDEXED_OUTBOUND_CHANNEL =
      "productionIndexedOutboundChannel";

  @NonNull
  private final AmqpInboundFactory inboundFactory;

  @NonNull
  private final AmqpOutboundFactory outboundFactory;

  @NonNull
  private final ProductionIndexerProperties properties;

  @Bean
  IntegrationFlow productionIndexingQueueToChannelIntegrationFlow() {
    return createInputFlow(
        PRODUCTION_INDEXING_INBOUND_CHANNEL,
        properties.getProductionIndexingInbound().getQueueName());
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
      ProductionIndexRequestV1CommandHandler productionIndexRequestV1CommandHandler,
      ProductionIndexRequestV2CommandHandler productionIndexRequestV2CommandHandler) {

    return new ProductionRequestCommandIntegrationFlowAdapter(
        productionIndexRequestV1CommandHandler,
        productionIndexRequestV2CommandHandler,
        PRODUCTION_INDEXING_INBOUND_CHANNEL,
        PRODUCTION_INDEXED_OUTBOUND_CHANNEL);
  }

  @Bean
  IntegrationFlow productionIndexedOutboundChannelToExchangeIntegrationFlow() {
    return createOutputFlow(
        PRODUCTION_INDEXED_OUTBOUND_CHANNEL,
        properties.getProductionIndexedOutbound().getExchangeName(),
        properties.getProductionIndexedOutbound().getRoutingKey());
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
