package com.silenteight.warehouse.test.client.gateway;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.amqp.core.DirectExchange;
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
  public static final String QA_INDEXING_OUTBOUND_CHANNEL =
      "qaIndexingOutboundChannel";
  public static final String ANALYSIS_EXPIRED_INDEXING_OUTBOUND_CHANNEL =
      "analysisExpiredIndexingOutboundChannel";
  public static final String ALERTS_EXPIRED_INDEXING_OUTBOUND_CHANNEL =
      "alertsExpiredIndexingOutboundChannel";

  @NonNull
  private final AmqpOutboundFactory outboundFactory;
  @NonNull
  private final IndexerClientIntegrationProperties properties;

  @Bean
  DirectChannel productionIndexingOutboundChannel() {
    return new DirectChannel();
  }

  @Bean
  GatewayProxyFactoryBean productionIndexClientGateway() {
    GatewayProxyFactoryBean factoryBean =
        new GatewayProxyFactoryBean(ProductionIndexClientGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(PRODUCTION_INDEXING_OUTBOUND_CHANNEL);
    return factoryBean;
  }

  @Bean
  GatewayProxyFactoryBean simulationIndexClientGateway() {
    GatewayProxyFactoryBean factoryBean =
        new GatewayProxyFactoryBean(SimulationIndexClientGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(SIMULATION_INDEXING_OUTBOUND_CHANNEL);
    return factoryBean;
  }

  @Bean
  GatewayProxyFactoryBean analysisExpiredClientGateway() {
    GatewayProxyFactoryBean factoryBean =
        new GatewayProxyFactoryBean(AnalysisExpiredClientGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(ANALYSIS_EXPIRED_INDEXING_OUTBOUND_CHANNEL);
    return factoryBean;
  }

  @Bean
  GatewayProxyFactoryBean alertsExpiredClientGateway() {
    GatewayProxyFactoryBean factoryBean =
        new GatewayProxyFactoryBean(AlertsExpiredClientGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(ALERTS_EXPIRED_INDEXING_OUTBOUND_CHANNEL);
    return factoryBean;
  }

  @Bean
  GatewayProxyFactoryBean qaIndexClientGateway() {
    GatewayProxyFactoryBean factoryBean =
        new GatewayProxyFactoryBean(QaIndexClientGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(QA_INDEXING_OUTBOUND_CHANNEL);
    return factoryBean;
  }

  @Bean
  DirectExchange productionCommandExchange() {
    return ExchangeBuilder
        .directExchange(properties.getProductionIndexingTestClientOutbound().getExchangeName())
        .build();
  }

  @Bean
  DirectExchange simulationCommandExchange() {
    return ExchangeBuilder
        .directExchange(properties.getSimulationIndexingTestClientOutbound().getExchangeName())
        .build();
  }

  @Bean
  DirectExchange retentionCommandExchange() {
    return ExchangeBuilder
        .directExchange(properties.getAnalysisExpiredIndexingTestClientOutbound()
            .getExchangeName())
        .build();
  }

  @Bean
  TopicExchange govEventExchange() {
    return ExchangeBuilder
        .topicExchange(properties.getQaIndexingTestClientOutbound().getExchangeName())
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

  @Bean
  IntegrationFlow analysisExpiredChannelToExchangeIntegrationFlow() {
    return createOutputFlow(
        ANALYSIS_EXPIRED_INDEXING_OUTBOUND_CHANNEL,
        properties.getAnalysisExpiredIndexingTestClientOutbound().getExchangeName(),
        properties.getAnalysisExpiredIndexingTestClientOutbound().getRoutingKey());
  }

  @Bean
  IntegrationFlow alertsExpiredChannelToExchangeIntegrationFlow() {
    return createOutputFlow(
        ALERTS_EXPIRED_INDEXING_OUTBOUND_CHANNEL,
        properties.getAlertsExpiredIndexingTestClientOutbound().getExchangeName(),
        properties.getAlertsExpiredIndexingTestClientOutbound().getRoutingKey());
  }

  @Bean
  IntegrationFlow qaIndexChannelToExchangeIntegrationFlow() {
    return createOutputFlow(
        QA_INDEXING_OUTBOUND_CHANNEL,
        properties.getQaIndexingTestClientOutbound().getExchangeName(),
        properties.getQaIndexingTestClientOutbound().getRoutingKey());
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
