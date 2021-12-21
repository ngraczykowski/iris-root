package com.silenteight.warehouse.production.handler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;
import com.silenteight.warehouse.production.handler.v1.ProductionRequestV1CommandHandler;
import com.silenteight.warehouse.production.handler.v2.ProductionRequestV2CommandHandler;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import javax.validation.Valid;

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
  @Valid
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
      ProductionRequestV1CommandHandler productionRequestV1CommandHandler,
      ProductionRequestV2CommandHandler productionRequestV2CommandHandler) {

    return new ProductionRequestCommandIntegrationFlowAdapter(
        productionRequestV1CommandHandler,
        productionRequestV2CommandHandler,
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
