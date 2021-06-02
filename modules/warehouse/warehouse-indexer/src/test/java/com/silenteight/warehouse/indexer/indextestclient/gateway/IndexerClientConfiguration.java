package com.silenteight.warehouse.indexer.indextestclient.gateway;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(IndexerClientIntegrationProperties.class)
public class IndexerClientConfiguration {

  public static final String PRODUCTION_INDEXING_OUTBOUND_CHANNEL =
      "productionIndexingOutboundChannel";
  public static final String SIMULATION_INDEXING_OUTBOUND_CHANNEL =
      "simulationIndexingOutboundChannel";

  @NonNull
  private final AmqpOutboundFactory outboundFactory;
  @NonNull
  private final IndexerClientIntegrationProperties properties;

  @Bean
  GatewayProxyFactoryBean productionIndexClientGateway() {
    GatewayProxyFactoryBean factoryBean =
        new GatewayProxyFactoryBean(IndexClientGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(PRODUCTION_INDEXING_OUTBOUND_CHANNEL);
    return factoryBean;
  }

  @Bean
  GatewayProxyFactoryBean simulationIndexClientGateway() {
    GatewayProxyFactoryBean factoryBean =
        new GatewayProxyFactoryBean(IndexClientGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(SIMULATION_INDEXING_OUTBOUND_CHANNEL);
    return factoryBean;
  }

  @Bean
  TopicExchange commonIndexExchange() {
    return ExchangeBuilder
        .topicExchange(properties.getProductionIndexingTestClientOutbound().getExchangeName())
        .build();
  }

  @Bean
  IntegrationFlow productionIndexChannelToExchangeIntegrationFlow() {
    return createOutputFlow(
        PRODUCTION_INDEXING_OUTBOUND_CHANNEL,
        properties.getProductionIndexingTestClientOutbound().getExchangeName(),
        properties.getProductionIndexingTestClientOutbound().getRoutingKey());
  }

  @Bean
  IntegrationFlow simulationIndexChannelToExchangeIntegrationFlow() {
    return createOutputFlow(
        SIMULATION_INDEXING_OUTBOUND_CHANNEL,
        properties.getSimulationIndexingTestClientOutbound().getExchangeName(),
        properties.getSimulationIndexingTestClientOutbound().getRoutingKey());
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
